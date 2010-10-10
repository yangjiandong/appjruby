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
require 'digest/sha1'

class User < ActiveRecord::Base

  FAVOURITE_PROPERTY_KEY='favourite'

  has_and_belongs_to_many :groups
  has_many :user_roles, :dependent => :delete_all
  has_many :properties, :foreign_key => 'user_id', :dependent => :delete_all
  has_many :active_filters, :include => 'filter', :order => 'order_index'
  has_many :filters, :dependent => :delete_all
   
  include Authentication
  include Authentication::ByPassword
  include Authentication::ByCookieToken
  include NeedAuthorization::ForUser
  include NeedAuthentication::ForUser

  validates_length_of       :name, :maximum => 200, :allow_blank => true, :allow_nil => true
  validates_length_of       :email, :maximum => 100, :allow_blank => true, :allow_nil => true

  validates_length_of       :password, :within => 4..40, :if => :password_required?
  validates_confirmation_of :password, :if => :password_required?

  validates_presence_of     :login
  validates_length_of       :login,    :within => 3..40
  validates_uniqueness_of   :login, :case_sensitive => true
  validates_format_of       :login,    :with => Authentication.login_regex, :message => Authentication.bad_login_message
  

  # HACK HACK HACK -- how to do attr_accessible from here?
  # prevents a user from submitting a crafted form that bypasses activation
  # anything else you want your user to change should be added here.
  attr_accessible :login, :email, :name, :password, :password_confirmation

  def name(login_if_nil=false)
    result=read_attribute :name
    result.blank? ? login : result
  end

  def login=(value)
    write_attribute :login, (value ? value.downcase : nil)
  end

  def email=(value)
    write_attribute :email, (value ? value.downcase : nil)
  end

  def available_groups
    Group.all - self.groups
  end

  def set_groups(new_groups=[])
    self.groups.clear

    new_groups=(new_groups || []).compact.uniq
    self.groups = Group.find(new_groups)
    save
  end

  def <=>(other)
    login<=>other.login
  end


  #---------------------------------------------------------------------
  # USER PROPERTIES
  #---------------------------------------------------------------------
  def property(key)
    properties().each do |p|
      return p if (p.key==key)
    end
    nil
  end

  def property_value(key)
    prop=property(key)
    prop ? prop.value : nil
  end

  def set_property(options)
    key=options[:prop_key]
    prop=property(key)
    if prop
      prop.attributes=options
      prop.user_id=id
      prop.save!
    else
      prop=Property.new(options)
      prop.user_id=id
      properties<<prop
    end
  end

  def delete_property(key)
    prop=property(key)
    if prop
      properties.delete(prop)
    end
  end
  
  #---------------------------------------------------------------------
  # FAVOURITES
  #---------------------------------------------------------------------
  
  def favourite_ids
    @favourite_ids ||=
      begin
        properties().select{|p| p.key==FAVOURITE_PROPERTY_KEY}.map{|p| p.resource_id}
      end
    @favourite_ids
  end

  def favourites
    favourite_ids.size==0 ? [] : Project.find(:all, :conditions => ['id in (?) and enabled=?', favourite_ids, true])
  end

  def add_favourite(resource_key)
    favourite=Project.by_key(resource_key)
    if favourite
      delete_favourite(favourite.id)
      properties().create(:prop_key => FAVOURITE_PROPERTY_KEY, :user_id => id, :resource_id => favourite.id)
    end
    favourite
  end

  def delete_favourite(resource_key)
    rid=resource_key
    if resource_key.is_a?(String)
      resource=Project.by_key(resource_key)
      rid = resource.id if resource
    end
    if rid
      props=properties().select{|p| p.key==FAVOURITE_PROPERTY_KEY && p.resource_id==rid}
      if props.size>0
        properties().delete(props)
        return true
      end
    end
    false
  end

  def favourite?(resource_id)
    favourite_ids().include?(resource_id.to_i)
  end
end
