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


#
# Use SLF4J instead of Rails BufferedLogger.
# Add the following property to the Rails environment :
#
# config.logger = Slf4jLogger.new
#
# Notes :
# - logger key is 'rails'
# - silence is not implemented
# - level FATAL does not exist in SLF4J. It's linked to ERROR level.
#
class Slf4jLogger
  def initialize(logger_name='rails')
    @logger = Java::OrgSlf4j::LoggerFactory::getLogger(logger_name);
  end

  attr_accessor :level
  
  # todo
  def silence(temporary_level = ERROR)
    yield self
  end

  def debug?
    @logger.isDebugEnabled()
  end

  def info?
    @logger.isInfoEnabled()
  end

  def warn?
    @logger.isWarnEnabled()
  end

  def error?
    @logger.isErrorEnabled()
  end

  def fatal?
    @logger.isErrorEnabled()
  end

  def debug(message = nil, progname = nil, &block)
    @logger.debug(full_message(message, &block))
  end

  def info(message = nil, progname = nil, &block)
    @logger.info(full_message(message, &block))
  end

  def warn(message = nil, progname = nil, &block)
    @logger.warn(full_message(message, &block))
  end

  def error(message = nil, progname = nil, &block)
    @logger.error(full_message(message, &block))
  end

  def fatal(message = nil, progname = nil, &block)
    @logger.error(full_message(message, &block))
  end

  def flush

  end

  def auto_flushing=(period)

  end

  def close

  end

  def silence(temporary_level = ERROR)
    # todo
  end

  private

  def full_message(message, &block)
    if message.nil?
      if block_given?
        message = yield
      end
    end
    message.to_s
  end
end