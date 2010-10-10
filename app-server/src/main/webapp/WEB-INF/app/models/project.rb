#
# Sonar, entreprise quality control tool.
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
class Project < ActiveRecord::Base
  include Comparable
  include Resourceable
  
  has_many :snapshots
  has_many :processed_snapshots, :class_name => 'Snapshot', :conditions => "status='#{Snapshot::STATUS_PROCESSED}'", :order => 'created_at asc'
  has_many :events, :foreign_key => 'resource_id', :order => 'event_date DESC'
  has_many :project_links, :dependent => :delete_all, :order => 'link_type'
  belongs_to :profile, :class_name => 'Profile', :foreign_key => 'profile_id'
  has_many :user_roles, :foreign_key => 'resource_id'
  has_many :group_roles, :foreign_key => 'resource_id'


  def self.by_key(k)
    begin
      ki=Integer(k)
      Project.find(ki)
    rescue
      Project.find(:first, :conditions => {:kee => k})
    end
  end

  def last_snapshot
    @last_snapshot ||=
      begin
        snapshot=Snapshot.find(:first, :conditions => {:islast => true, :project_id => id})
        if snapshot
          snapshot.project=self
        end
        snapshot
      end
  end
  
  def events_with_snapshot
    events.select{|event| not event.snapshot_id.nil?}
  end
  
  def key
    kee
  end

  def links
    project_links
  end
  
  def link(type)
    # to_a avoids conflicts with ActiveRecord:Base.find
    links.to_a.find{ |l| l.link_type==type}
  end 
  
  def custom_links
    links.select {|l| l.custom?}
  end
  
  def standard_links
    links.reject {|l| l.custom?}
  end

  def chart_measures(metric_id)
    sql = Project.send(:sanitize_sql, ['select s.created_at as created_at, m.value as value ' +
          ' from project_measures m, snapshots s ' +
          ' where s.id=m.snapshot_id and ' +
          " s.status='%s' and " +
          ' s.project_id=%s and m.metric_id=%s ', Snapshot::STATUS_PROCESSED, self.id, metric_id] ) +
      ' and m.rules_category_id IS NULL and m.rule_id IS NULL and m.rule_priority IS NULL' +
      ' order by s.created_at'
    create_chart_measures( Project.connection.select_all( sql ), 'created_at', 'value' )
  end

  def <=>(other)
    kee <=> other.kee
  end

  def name(long_if_defined=false)
    if long_if_defined
      long_name || read_attribute(:name)
    else
      read_attribute(:name)
    end
  end

  def fullname
    name
  end
  
  def branch
    if project? || module?
      s=kee.split(':')
      if s.size>=3
        return s[2]
      end 
    end
    nil
  end

  def resource_id_for_authorization
    if library?
      # no security on libraries
      nil
    elsif set?
      self.root_id || self.id
    elsif last_snapshot
      last_snapshot.resource_id_for_authorization
    else
      nil
    end
  end

  def path_name
    last_snapshot ? last_snapshot.path_name : nil
  end

  private

  def create_chart_measures(results, date_column_name, value_column_name)
    chart_measures = []
    if results and results.first != nil
      # :sanitize_sql is protected so its behaviour cannot be predicted exactly,
      # the jdbc active record impl adapter returns a db typed objects array
      # when regular active record impl return string typed objects
      if results.first[date_column_name].class == Time
        results.each do |hash|
          chart_measures << ChartMeasure.new( hash[date_column_name], hash[value_column_name] )
        end
      else
        results.each do |hash|
          chart_measures << ChartMeasure.new( Time.parse( hash[date_column_name] ), hash[value_column_name].to_d )
        end
      end
    end
    chart_measures
  end
end
