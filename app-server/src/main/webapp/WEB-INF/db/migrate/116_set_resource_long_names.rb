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
class SetResourceLongNames < ActiveRecord::Migration

  def self.up
    update('TRK')
    update('BRC')
    update('PAC')
    update('DIR')
    update('LIB')
    update('CLA')
    update('FIL')
    update('UTS')
    update('VW')
    update('SVW')
  end

  private

  def self.update(qualifier)
    Project.connection.update("update projects set long_name=name where long_name is null and qualifier='#{qualifier}'")
  end
end
