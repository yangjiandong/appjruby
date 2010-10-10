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
module AlertsHelper
  NUMERIC_THRESOLD_OPERATORS = {'is less than' => '<', 'is greater than' => '>', 'equals' => '=', 'is not' => '!=' }
  BOOLEAN_THRESOLD_OPERATORS = {'is' => '='}
  STRING_THRESOLD_OPERATORS = {'equals' => '=', 'is not' => '!=', 'is greater than' => '>', 'is less than' => '<'}
  LEVEL_THRESOLD_OPERATORS = {'is' => '=', 'is not' => '!='}

  def operators_for_select(alert)
    if alert.metric.nil?
      {}
    elsif alert.metric.numeric?
      NUMERIC_THRESOLD_OPERATORS

    elsif alert.metric.val_type==Metric::VALUE_TYPE_BOOLEAN
      BOOLEAN_THRESOLD_OPERATORS

    elsif alert.metric.val_type==Metric::VALUE_TYPE_STRING
      STRING_THRESOLD_OPERATORS

    elsif alert.metric.val_type==Metric::VALUE_TYPE_LEVEL
      LEVEL_THRESOLD_OPERATORS
    else
      {}
    end
  end

  def default_operator(alert)
    if alert.metric.nil?
      nil
      
    elsif alert.metric.numeric?
      if alert.metric.qualitative?
        alert.metric.direction>0 ? '<' : '>'
      else
        '>'
      end

    elsif alert.metric.val_type==Metric::VALUE_TYPE_BOOLEAN
      '='

    elsif alert.metric.val_type==Metric::VALUE_TYPE_STRING
      '='

    elsif alert.metric.val_type==Metric::VALUE_TYPE_LEVEL
      '='

    else
      nil
    end
  end


  def value_field(alert, value, fieldname)
    if alert.metric.nil?
      text_field_tag fieldname, value, :size => 5
      
    elsif alert.metric.numeric?
      text_field_tag fieldname, value, :size => 5

    elsif alert.metric.val_type==Metric::VALUE_TYPE_BOOLEAN
      select_tag fieldname, {'' => '', 'Yes' => '1', 'No' => '0'}

    elsif alert.metric.val_type==Metric::VALUE_TYPE_STRING
      text_field_tag fieldname, value, :size => 5

    elsif alert.metric.val_type==Metric::VALUE_TYPE_LEVEL
      select_tag fieldname, {'' => '', 'OK' => Metric::TYPE_LEVEL_OK, 'Error' => Metric::TYPE_LEVEL_ERROR, 'Warning' => Metric::TYPE_LEVEL_WARN}
    else
      hidden_field_tag fieldname, value
    end
  end

end