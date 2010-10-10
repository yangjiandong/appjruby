ActionController::Routing::Routes.draw do |map|
  map.connect 'users/select_group', :controller => 'users', :action => 'select_group'
  map.connect 'users/set_groups', :controller => 'users', :action => 'set_groups'
  map.resources :users
  map.resource :session

  # used only for the events widget
  map.resources :events
  
  map.namespace :api do |api|
    api.resources :events, :only => [:index, :show, :create, :destroy]
    api.resources :user_properties, :only => [:index, :show, :create, :destroy], :requirements => { :id => /.*/ }
    api.resources :favorites, :only => [:index, :show, :create, :destroy], :requirements => { :id => /.*/ }
  end
  
  map.connect 'api/metrics', :controller => 'api/metrics', :action => 'index', :conditions => { :method => :get }
  map.connect 'api/metrics/:id', :controller => 'api/metrics', :action => 'show', :conditions => { :method => :get }
  map.connect 'api/metrics/:id', :controller => 'api/metrics', :action => 'create', :conditions => { :method => :post }
  map.connect 'api/metrics/:id', :controller => 'api/metrics', :action => 'update', :conditions => { :method => :put }
  map.connect 'api/metrics/:id', :controller => 'api/metrics', :action => 'destroy', :conditions => { :method => :delete }
  map.connect 'api/server/:action', :controller => 'api/server'
  map.connect 'api/resoures', :controller => 'api/resources', :action => 'index'
  map.connect 'api/sources', :controller => 'api/sources', :action => 'index'
  map.connect 'api/violations', :controller => 'api/violations', :action => 'index'
  map.resources 'rules', :path_prefix => 'api', :controller => 'api/rules'
  map.resources 'properties', :path_prefix => 'api', :controller => 'api/properties', :requirements => { :id => /.*/ }


  # home page
  map.home '', :controller => 'filters', :action => 'index'
  map.root :controller => 'filters', :action => 'index'

  # page plugins
  map.connect 'plugins/configuration/:page', :controller => 'plugins/configuration', :action => 'index', :requirements => { :page => /.*/ }
  map.connect 'plugins/home/:page', :controller => 'plugins/home', :action => 'index', :requirements => { :page => /.*/ }
  map.connect 'plugins/resource/:id', :controller => 'plugins/resource', :action => 'index', :requirements => { :id => /.*/ }
  
  # to refactor
  map.connect 'charts/:action/:project_id/:metric_id', :controller => 'charts'
  map.connect 'rules_configuration/:action/:language/:name/:plugin.:format', :controller => 'rules_configuration'

  # Install the default route as the lowest priority.
  map.connect ':controller/:action/:id', :requirements => { :id => /.*/ }

end
