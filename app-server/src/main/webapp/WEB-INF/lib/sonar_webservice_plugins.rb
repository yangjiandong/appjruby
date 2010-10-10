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

module ActionController
  module Routing
    class RouteSet
      def load_sonar_plugins_routes
        logger=Slf4jLogger.new('org.sonar.INFO')
        logger.info("Loading web services...")
        web_service_plugins = Java::OrgSonarServerUi::JRubyFacade.new.getRubyRailsWebservices()
        web_service_plugins.each do |plugin|
          logger.info("Loading webservice #{plugin.getId()}")
          eval(plugin.getTemplate())
          route = add_route("api/plugins/#{plugin.getId()}/:action/:id", {:controller => "api/#{plugin.getId()}", :requirements => { :id => /.*/ }})
          logger.info("Loaded webservice #{plugin.getId()} => #{route}")
        end
      end
    end
  end
end

# support for routes reloading in dev mode
if ENV['RAILS_ENV'] == "development"
  module ActionController
    module Routing
      class RouteSet
        alias rails_reload reload

        def reload
          rails_reload
          load_sonar_plugins_routes
        end
      end
    end
  end
end