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
class Group < ActiveRecord::Base

  has_and_belongs_to_many :users
  has_many :group_roles, :dependent => :delete_all
  
  validates_presence_of     :name
  validates_length_of       :name,    :within => 1..40
  validates_length_of       :description,    :within => 0..200
  validates_uniqueness_of   :name

  # all the users that are NOT members of this group
  def available_users
    User.all - users
  end

  def set_users(new_users=[])
    self.users.clear
    
    new_users=(new_users || []).compact.uniq
    self.users = User.find(new_users)
    save
  end

  def <=>(other)
    return 1 if other.nil?
    name<=>other.name
  end
end
