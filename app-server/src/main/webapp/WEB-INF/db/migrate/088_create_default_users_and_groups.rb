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
class CreateDefaultUsersAndGroups < ActiveRecord::Migration

  def self.up
  	create_administrators
    create_users
  end

  def self.down
	  
  end

  private
  def self.create_administrators
    administrators=Group.create(:name => 'sonar-administrators', :description => 'System administrators')
    GroupRole.create(:group_id => administrators.id, :role => 'admin')

    admin=User.find_by_login('admin')
    admin.groups<<administrators
  	admin.save!       	 
  end

  def self.create_users
    users=Group.create(:name => 'sonar-users', :description => 'Any new users created will automatically join this group')

    # The user 'admin' is considered as a user
    admin=User.find_by_login('admin')
    admin.groups<<users
    admin.save!
  end
end
