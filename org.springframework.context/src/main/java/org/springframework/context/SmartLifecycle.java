/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context;

/**
 * An extension of the Lifecycle interface for those beans that require to be
 * started upon ApplicationContext refresh and/or shutdown in a particular order.
 * 
 * @author Mark Fisher
 * @since 3.0
 */
public interface SmartLifecycle extends Lifecycle {

	/**
	 * Return whether this Lifecycle component should be started automatically
	 * by the container when the ApplicationContext is refreshed. A value of
	 * "false" indicates that the component is intended to be started manually.
	 */
	boolean isAutoStartup();

	/**
	 * Return the phase within which this Lifecycle component should be started
	 * and stopped. The startup process begins with the <i>lowest</i> phase
	 * value and ends with the <i>highest</i> phase value (Integer.MIN_VALUE is
	 * the lowest possible, and Integer.MAX_VALUE is the highest possible). The
	 * shutdown process will apply the reverse order. Any components with the
	 * same value will be arbitrarily ordered within the same phase.
	 * <p>
	 * Example: if component B depends on component A having already started, then
	 * component A should have a lower phase value than component B. During the
	 * shutdown process, component B would be stopped before component A.
	 * <p>
	 * Any Lifecycle components within the context that do not also implement
	 * SmartLifecycle will be treated as if they have a phase value of 0. That
	 * way a SmartLifecycle implementation may start before those Lifecycle
	 * components if it has a negative phase value, or it may start after
	 * those components if it has a positive phase value.
	 * <p>
	 * Any explicit "depends-on" relationship will take precedence over
	 * the phase order such that the dependent bean always starts after its
	 * dependency and always stops before its dependency.
	 */
	int getPhase();

	/**
	 * Indicates that a Lifecycle component must stop if it is currently running.
	 * The provided callback is used by the LifecycleProcessor to support an
	 * ordered, and potentially concurrent, shutdown of all components having a
	 * common shutdown order value. The callback <b>must</b> be executed after
	 * the SmartLifecycle component does indeed stop.
	 */
	void stop(Runnable callback);

}
