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
class AddRulesCardinality < ActiveRecord::Migration

  def self.up
    add_column 'rules', 'cardinality', :string, :null => true, :limit => 10
    add_column 'rules', 'parent_id', :integer, :null => true
    add_column 'rules_parameters', 'default_value', :string, :null => true, :limit => 4000

    Rule.reset_column_information
    Rule.update_all(Rule.sanitize_sql_for_assignment({:cardinality => 'SINGLE'}))
  end

end
