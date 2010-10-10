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
require 'java'

class DatabaseVersion

  class DeprecatedSchemaInfo < ActiveRecord::Base
    set_table_name 'schema_info'
  end

  def self.current_version
    result=0
    begin
      result=ActiveRecord::Migrator.current_version
    rescue
    end

    if result==0
      begin
        result=DeprecatedSchemaInfo.find(:first).version
      rescue
      end
    end
    result
  end

  def self.target_version
    files = Dir["#{migrations_path}/[0-9]*_*.rb"].sort
    files.last.scan(/([0-9]+)_[_a-z0-9]*.rb/).first[0].to_i
  end

  def self.migrations_path
    File.dirname(__FILE__).to_s + "/../db/migrate/"
  end

  $uptodate = false

  def self.uptodate?
    unless $uptodate
      $uptodate = (current_version>=target_version)
    end
    $uptodate
  end

  def self.setup
    ActiveRecord::Migrator.migrate(migrations_path)
    Java::OrgSonarServerPlatform::Platform.getInstance().start()
    load_plugin_webservices()
  end

  def self.load_plugin_webservices
    ActionController::Routing::Routes.load_sonar_plugins_routes
  end

  def self.automatic_setup
    if current_version<=0
      setup
    end
    if uptodate?
      load_plugin_webservices()
    end
    uptodate?
  end

  def self.connected?
    ActiveRecord::Base.connected?
  end
end