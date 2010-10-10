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
class Sonar::TimemachineRow
  attr_accessor :metric

  def initialize(metric)
    @metric=metric
    @measure_by_sid={}
  end

  def add_measure(measure)
    @measure_by_sid[measure.snapshot_id]=measure
  end

  def measure(snapshot)
    @measure_by_sid[snapshot.id]
  end

  def domain
    @metric.domain.nil? ? "" : @metric.domain
  end

  def <=>(other)
    (self.domain <=> other.domain).nonzero? || (self.metric.description <=> other.metric.description)
  end

  def sparkline_url
    if metric.numeric? and @measure_by_sid.size>1
      values=[]
      @measure_by_sid.values.each do |measure|
        # date.to_f does not works under oracle
        values << measure.snapshot.created_at.to_s(:number)
        values << (measure.value.nil? ? 0 : measure.value)
      end
      "/chart?cht=sl&chdi=80x15&chv=" + values*',' + '&.png'
     else
       nil
     end
  end
end
