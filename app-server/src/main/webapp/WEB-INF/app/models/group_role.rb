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
class GroupRole < ActiveRecord::Base

  belongs_to :group
  belongs_to :resource, :class_name => 'Project', :foreign_key => "resource_id"
  
  def self.grant_groups(group_ids, role, resource_id=nil)
    resource_id=(resource_id.blank? ? nil : resource_id.to_i)
    if resource_id
      GroupRole.delete_all(["role=? and resource_id=?", role, resource_id])
    else
      GroupRole.delete_all(["role=? and resource_id is null", role])
    end
    if group_ids
      group_ids.compact.uniq.each  do |group_id|
        GroupRole.create(:group_id => group_id, :role => role, :resource_id => resource_id)
      end
    end
  end  
end
