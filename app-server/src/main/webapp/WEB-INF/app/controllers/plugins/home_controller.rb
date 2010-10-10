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
class Plugins::HomeController < ApplicationController

  SECTION=Navigation::SECTION_HOME

  def index
    page_id=params[:page]
    @page_proxy=java_facade.getPage(page_id)

    authorized=@page_proxy.getUserRoles().size==0
    unless authorized
      @page_proxy.getUserRoles().each do |role|
        # 'admin' is the only role to check
        authorized=(role!='admin' || has_role?(role))
        break if authorized
      end
    end
   
    if authorized
      @page=@page_proxy.getTarget()
      if @page_proxy.isGwt()
        @gwt_id = @page.getGwtId()
        render :template => 'gwt/page'
      else
        render :template => 'plugins/rails_page'
      end
    else
      access_denied
    end
  end
    
end
