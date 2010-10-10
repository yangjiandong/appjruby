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
class Event < ActiveRecord::Base

  validates_presence_of    :event_date
  validates_length_of      :name, :within => 1..50
  validates_length_of      :category, :within => 1..50

  belongs_to :resource, :class_name => 'Project', :foreign_key => 'resource_id'
  belongs_to :snapshot

  before_save :populate_snapshot

  def fullname
    if category
      "#{category} #{name}"
    else
      name
    end
  end

  def populate_snapshot
    self.snapshot=Snapshot.snapshot_by_date(resource_id, event_date)
  end
end
