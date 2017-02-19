/** 
 * <h3>Location Tracking System of APS application</h3> <br><br>
 * The locationTracking package provides function for
 * <br>
 * 1. Finding out the user consent for participating location tracking <br>
 * 2. Track user location based on the time range set by the clinic or research group.
 * </br>
 * <p> This package implements a location alarm receiver which is a broadcast receiver starts at the start of central server data 
 * transfer and registers a location listener with the location manager to obtain periodic location coordinates.
 * <ul>
 * <li> When registering the listener, make user services that provide location are active. If not, display dialog that leads user to devices settings.</li>
 * <li> The alarm stops while the tracking is stopped by the user or the time of tracking is over.</li>
 * <li> While implementing, used both fine and coarse location so that user consent provider can be used to get user location.</li>
 * <li> This package also contains the implementation of LocationBootReceiver.java which confirms that the location tracking process will not stop if the
 * devices restarted.</li>
 * </ul>
 * @since 1.0
 * @author Mahbubur Rahman
 * @version 1.0
 */

package com.emmes.aps.locationtracking;