#
# Sonar, open source software quality management tool.
# Copyright (C) 2009 SonarSource SA
# mailto:contact AT sonarsource DOT com
#
# Sonar is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 3 of the License, or (at your option) any later version.
#
# Sonar is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with Sonar; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
#
class Api::ResourcesController < Api::ApiController

  def index
    begin
      resource_id=params[:resource]
      if resource_id
        @resource=Project.by_key(resource_id)
        @snapshot=(@resource ? @resource.last_snapshot : nil)
        raise ApiException.new(404, "Resource [#{resource_id}] not found") if @snapshot.nil?
      else
        @snapshot=nil
        if params['scopes'].blank? && params['qualifiers'].blank?
          params['scopes']=Project::SCOPE_SET
          params['qualifiers']=Project::QUALIFIER_PROJECT
        end
      end

      # ---------- PARAMETERS
      snapshots_conditions=['snapshots.islast=:islast']
      snapshots_values={:islast => true}

      load_measures=false
      measures_conditions=[]
      measures_values={}
      measures_order = nil
      measures_limit = nil
      measures_by_sid={}
      measures=nil
      rules_by_id=nil

      if params['scopes']
        snapshots_conditions << 'snapshots.scope in (:scopes)'
        snapshots_values[:scopes]=params['scopes'].split(',')
      end

      if params['qualifiers']
        snapshots_conditions << 'snapshots.qualifier in (:qualifiers)'
        snapshots_values[:qualifiers]=params['qualifiers'].split(',')
      end

      if @snapshot
        depth=(params['depth'] ? params['depth'].to_i : 0)
        if depth==0
          snapshots_conditions << 'snapshots.id=:sid'
          snapshots_values[:sid]=@snapshot.id

        elsif depth>0
          snapshots_conditions << 'snapshots.root_snapshot_id=:root_sid'
          snapshots_values[:root_sid] = (@snapshot.root_snapshot_id || @snapshot.id)

          snapshots_conditions << 'snapshots.path LIKE :path'
          snapshots_values[:path]="#{@snapshot.path}#{@snapshot.id}.%"

          snapshots_conditions << 'snapshots.depth=:depth'
          snapshots_values[:depth] = @snapshot.depth + depth
        
        else
          # negative : all the resource tree
          snapshots_conditions << '(snapshots.id=:sid OR (snapshots.root_snapshot_id=:root_sid AND snapshots.path LIKE :path))'
          snapshots_values[:sid]=@snapshot.id
          snapshots_values[:root_sid] = (@snapshot.root_snapshot_id || @snapshot.id)
          snapshots_values[:path]="#{@snapshot.path}#{@snapshot.id}.%"
        end
      end

      if params['metrics'] && params['metrics']!='false'
        set_backward_compatible
        load_measures=true

        if params['metrics']!='true'
          metrics=Metric.by_keys(params[:metrics].split(','))
          measures_conditions <<'project_measures.metric_id IN (:metrics)'
          measures_values[:metrics]=metrics.select{|m| m.id}
          if metrics.size==1
            measures_limit = [params[:limit].to_i,500].min if params[:limit]
            measures_order = "project_measures.value #{'DESC' if metrics.first.direction<0}"
          end
        end
      
        add_rule_filters(measures_conditions, measures_values)
        add_characteristic_filters(measures_conditions, measures_values)

        measures=ProjectMeasure.find(:all,
          :joins => :snapshot,
          :select => select_columns_for_measures,
          :conditions => [ (snapshots_conditions + measures_conditions).join(' AND '), snapshots_values.merge(measures_values)],
          :order => measures_order,
          :limit => measures_limit)

        measures.each do |measure|
          measures_by_sid[measure.snapshot_id]||=[]
          measures_by_sid[measure.snapshot_id]<<measure
        end

        if measures_limit && !measures.empty?
          snapshots_conditions << 'snapshots.id IN (:sids)'
          snapshots_values[:sids]=measures_by_sid.keys
        end

        # load coding rules
        rules_by_id={}
        rule_ids=measures.map{|m| m.rule_id}.compact.uniq
        unless rule_ids.empty?
          Rule.find(rule_ids).each do |rule|
            rules_by_id[rule.id]=rule
          end
        end
      end

      # ---------- LOAD RESOURCES
      if params['scopes']
        # optimization : add constraint to projects table
        snapshots_conditions << 'projects.scope in (:scopes)'
      end

      if params['qualifiers']
        # optimization : add constraint to projects table
        snapshots_conditions << 'projects.qualifier in (:qualifiers)'
      end

      snapshots_including_resource=Snapshot.find(:all, :conditions => [snapshots_conditions.join(' AND '), snapshots_values], :include => 'project')

      # ---------- APPLY SECURITY - remove unauthorized resources
      snapshots_including_resource=select_authorized(:user, snapshots_including_resource)

      # ---------- PREPARE RESPONSE
      resource_by_sid={}
      snapshots_by_rid={}
      snapshots_including_resource.each do |snapshot|
        resource_by_sid[snapshot.id]=snapshot.project
        snapshots_by_rid[snapshot.project_id]=snapshot
      end


      # ---------- SORT RESOURCES
      if load_measures && measures_order && measures && !measures.empty?
        # resources sorted by measures
        sorted_resources=measures.map do |measure|
          resource_by_sid[measure.snapshot_id]
        end
      else
        # no specific sort
        sorted_resources=resource_by_sid.values
      end

      sorted_resources=sorted_resources.uniq.compact
    
      # ---------- FORMAT RESPONSE
      objects={ :sorted_resources => sorted_resources, :snapshots_by_rid => snapshots_by_rid, :measures_by_sid => measures_by_sid, :params => params, :rules_by_id => rules_by_id}
      respond_to do |format| 
        format.json { render :json => jsonp(to_json(objects)) }
        format.xml  { render :xml => to_xml(objects) }
        format.text { render :text => text_not_supported }
      end
    rescue ApiException => e
      render_error(e.msg, e.code)
    end
  end

  private
  
  def set_backward_compatible
    # backward-compatibility with sonar 1.9
    if params['filter_rules']
      (params['filter_rules']=='true') ? params['rules']='false' : params['rules']='true'
    end
    
    if params['filter_rules_cats']
      (params['filter_rules_cats']=='true') ? params['rule_categories']='false' : params['rule_categories']='true'
    end
    
    if params['metrics']
      if params['metrics'].include? 'mandatory_violations'
        params['metrics']=params['metrics'].gsub(/mandatory_violations/, 'violations')
        params['rule_priorities']='MAJOR'
      elsif params['metrics'].include? 'optional_violations'
        params['metrics']=params['metrics'].gsub(/optional_violations/, 'violations')
        params['rule_priorities']='INFO'
      end
    end
  end
  
  def select_columns_for_measures
    select_columns='project_measures.id,project_measures.value,project_measures.metric_id,project_measures.snapshot_id,project_measures.rule_id,project_measures.rules_category_id,project_measures.rule_priority,project_measures.text_value,project_measures.characteristic_id'
    if params[:includetrends]=='true'
      select_columns+=',project_measures.tendency,project_measures.diff_value_1,project_measures.diff_value_2,project_measures.diff_value_3'
    end
    if params[:includealerts]=='true'
      select_columns+=',project_measures.alert_status,project_measures.alert_text'
    end
    if params[:includedescriptions]=='true'
      select_columns+=',project_measures.url,project_measures.description'
    end
    select_columns
  end
  
  def add_rule_filters(measures_conditions, measures_values)
    param_rules = params['rules'] || 'false'
    if param_rules=='true'
      measures_conditions << "project_measures.rule_id IS NOT NULL"
      
    elsif param_rules=='false'
      measures_conditions << "project_measures.rule_id IS NULL"
    else
      rule_ids=param_rules.split(',').map do |key_or_id|
        Rule.to_i(key_or_id)
      end
      measures_conditions << 'project_measures.rule_id IN (:rule_ids)'
      measures_values[:rule_ids]=rule_ids.compact
    end
      
    param_categs=params['rule_categories'] || 'false'
    if param_categs=='true'
      measures_conditions << "project_measures.rules_category_id IS NOT NULL"

    elsif param_categs=='false'
      measures_conditions << "project_measures.rules_category_id IS NULL" if param_rules=='false'
    else
      measures_conditions << "project_measures.rules_category_id IN (:categ_ids)"
      measures_values[:categ_ids]=param_categs.split(',').map do |c|
        categ=RulesCategory.by_key(c)
        categ ? categ.id : -1
      end.compact
    end
    
    param_priorities = params['rule_priorities'] || 'false'
    if param_priorities=='true'
      measures_conditions << "project_measures.rule_priority IS NOT NULL"
    elsif param_priorities=='false'
      measures_conditions << "project_measures.rule_priority IS NULL" if param_rules=='false'
    else
      measures_conditions << "project_measures.rule_priority IN (:priorities)"
      measures_values[:priorities]=param_priorities.split(',').map do |p|
        Sonar::RulePriority.id(p)
      end.compact
    end
  end
  
  def add_characteristic_filters(measures_conditions, measures_values)
    @characteristics=[]
    @characteristic_by_id={}
    if params[:model].present? && params[:characteristics].present?
      @characteristics=Characteristic.find(:all,
        :select => 'characteristics.id,characteristics.kee,characteristics.name',
        :joins => :quality_model,
        :conditions => ['quality_models.name=? AND characteristics.kee IN (?)', params[:model], params[:characteristics].split(',')])
      if @characteristics.empty?
        measures_conditions<<'project_measures.characteristic_id=-1'
      else
        @characteristics.each { |c| @characteristic_by_id[c.id]=c }
        measures_conditions<<'project_measures.characteristic_id IN (:characteristics)'
        measures_values[:characteristics]=@characteristic_by_id.keys
      end
    else
      measures_conditions<<'project_measures.characteristic_id IS NULL'
    end
  end

  def to_json(objects)
    resources = objects[:sorted_resources]
    snapshots_by_rid = objects[:snapshots_by_rid]
    measures_by_sid = objects[:measures_by_sid]
    rules_by_id = objects[:rules_by_id]
    params = objects[:params]

    result=[]
    resources.each do |resource|
      snapshot=snapshots_by_rid[resource.id]
      result<<resource_to_json(resource, snapshot, measures_by_sid[snapshot.id], rules_by_id, params)
    end
    result
  end
  
  def to_xml(objects)
    resources = objects[:sorted_resources]
    snapshots_by_rid = objects[:snapshots_by_rid]
    measures_by_sid = objects[:measures_by_sid]
    rules_by_id = objects[:rules_by_id]
    params = objects[:params]

    xml = Builder::XmlMarkup.new(:indent => 0)
    xml.instruct!
  
    xml.resources do
      resources.each do |resource|
        snapshot=snapshots_by_rid[resource.id]
        resource_to_xml(xml, resource, snapshot, measures_by_sid[snapshot.id], rules_by_id, params)
      end
    end
  end

  def resource_to_json(resource, snapshot, measures, rules_by_id, options={})
    verbose=(options[:verbose]=='true')
    include_alerts=(options[:includealerts]=='true')
    include_trends=(options[:includetrends]=='true')
    include_descriptions=(options[:includedescriptions]=='true')
    
    json = {
      'id' => resource.id,
      'key' => resource.key,
      'name' => resource.name,
      'scope' => resource.scope,
      'qualifier' => resource.qualifier,
      'date' => format_datetime(snapshot.created_at)}
    json['lname']=resource.long_name if resource.long_name
    json['lang']=resource.language if resource.language
    json['version']=snapshot.version if snapshot.version
    json['branch']=resource.branch if resource.branch
    json['description']=resource.description if resource.description
    json['copy']=resource.copy_resource_id if resource.copy_resource_id
    if measures
      json_measures=[]
      json['msr']=json_measures
      measures.each do |measure|
        json_measure={}
        json_measures<<json_measure
        json_measure[:key]=measure.metric.name
        json_measure[:name]=measure.metric.short_name if verbose
        json_measure[:val]=measure.value.to_f if measure.value
        json_measure[:frmt_val]=measure.formatted_value if measure.value
        json_measure[:data]=measure.data if measure.data
        json_measure[:description]=measure.description if include_descriptions && measure.description
        json_measure[:url]=measure.url if include_descriptions && measure.url
        if include_alerts
          json_measure[:alert]=measure.alert_status
          json_measure[:alert_text]=measure.alert_text
        end
        if include_trends && measure.tendency
          json_measure[:trend]=measure.tendency_qualitative
          json_measure[:var]=measure.tendency
        end
        if measure.rule_id
          rule = rules_by_id[measure.rule_id]
          json_measure[:rule_key] = rule.key if rule
          json_measure[:rule_name] = rule.name if rule
        end
        if measure.rules_category_id
          json_measure[:rule_category] = measure.category.name
        end
        if measure.rule_priority
          json_measure[:rule_priority] = Sonar::RulePriority.to_s(measure.rule_priority)
        end
        if measure.characteristic_id
          characteristic=@characteristic_by_id[measure.characteristic_id]
          json_measure[:ctic_key]=(characteristic ? characteristic.kee : '')
          json_measure[:ctic_name]=(characteristic ? characteristic.name : '')
        end
      end
    end
    json
  end

  def resource_to_xml(xml, resource, snapshot, measures, rules_by_id, options={})
    verbose=(options[:verbose]=='true')
    include_alerts=(options[:includealerts]=='true')
    include_trends=(options[:includetrends]=='true')
    include_descriptions=(options[:includedescriptions]=='true')
    
    xml.resource do
      xml.id(resource.id)
      xml.key(resource.key)
      xml.name(resource.name)
      xml.lname(resource.long_name) if resource.long_name
      xml.branch(resource.branch) if resource.branch
      xml.scope(resource.scope)
      xml.qualifier(resource.qualifier)
      xml.lang(resource.language) if resource.language
      xml.version(snapshot.version) if snapshot.version
      xml.date(format_datetime(snapshot.created_at))
      xml.description(resource.description) if include_descriptions && resource.description
      xml.copy(resource.copy_resource_id) if resource.copy_resource_id
      if measures
        measures.each do |measure|
          xml.msr do
            xml.key(measure.metric.name)
            xml.name(measure.metric.short_name) if verbose
            xml.val(measure.value.to_f) if measure.value
            xml.frmt_val(measure.formatted_value) if measure.value
            xml.data(measure.data) if measure.data
            xml.description(measure.description) if include_descriptions && measure.description
            xml.url(measure.url) if include_descriptions && measure.url
            if include_alerts
              xml.alert(measure.alert_status) if measure.alert_status
              xml.alert_text(measure.alert_text) if measure.alert_text
            end
            if include_trends && measure.tendency
              xml.trend(measure.tendency_qualitative)
              xml.var(measure.tendency)
            end
            if measure.rule_id
              rule = rules_by_id[measure.rule_id]
              xml.rule_key(rule.key) if rule
              xml.rule_name(rule.name) if rule
            end
            if measure.rules_category_id
             xml.rule_category(measure.category.name)
            end
            if measure.rule_priority
              xml.rule_priority(Sonar::RulePriority.to_s(measure.rule_priority))
            end
            if measure.characteristic_id
              characteristic=@characteristic_by_id[measure.characteristic_id]
              xml.ctic_key(characteristic ? characteristic.kee : '')
              xml.ctic_name(characteristic ? characteristic.name : '')
            end
          end
        end
      end
    end
  end
end