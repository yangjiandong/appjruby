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
# License along with {library}; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
#
 class ActiveRuleParameter < ActiveRecord::Base
 belongs_to :active_rule
 belongs_to :rules_parameter

 def name
  rules_parameter.name  
 end

 def parameter
   rules_parameter
 end

 def validate_on_update 
   rules_parameter.validate_value(value, errors, "value" )
 end
 
 def copy
   ActiveRuleParameter.new(:rules_parameter => rules_parameter, :value => value)
 end  

 end
