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
class ProjectLink < ActiveRecord::Base
  belongs_to :project
  
  LINK_HOMEPAGE = "homepage"
  LINK_CONTINUOUS_INTEGRATION = "ci"
  LINK_ISSUE_TRACKER = "issue"
  LINK_SCM_URL = "scm"
  LINK_SCM_RO_CONNECTION = "scm_ro"
  LINK_SCM_DEV_CONNECTION = "scm_dev"

  CORE_LINK_KEYS = [LINK_HOMEPAGE,LINK_CONTINUOUS_INTEGRATION,LINK_ISSUE_TRACKER,LINK_SCM_URL,LINK_SCM_DEV_CONNECTION]
  
 def custom?
    !CORE_LINK_KEYS.include?(key)
  end

  def type
    link_type
  end

  def key
    link_type
  end

  def self.name_to_key(s)
    s.tr(' ', '_')[0..19]
  end

  def icon
    if custom?
      'links/external.png'
    else
      "links/#{key}.png"
    end
  end

  def to_hash_json
    {'type' => link_type, 'name' => name, 'url' => href}
  end

  def to_xml(xml)
    xml.link do
      xml.type(link_type)
      xml.name(name)
      xml.url(href)
    end
  end

  def <=>(other)
    if name.nil?
      -1
    elsif other.name.nil?
      1
    else
      name.downcase <=> other.name.downcase
    end
  end
end