# LocationAlarm
Alarm App working with Geo Locations

## Getting Started

Idea of this Application is to execute Alarms when user Enter or Exit from Geofence region. This Application includes below mentioned features -

* Create Alarm - User can create new Alarm by enetring Alarm name, Location(for Geofence Trigger), Ringtone for Notification, Vibration enable/disable.
* Update Alarm - User can enable/diable specific Alarm that will also enable/disable the Geofence.
* Track Location - Location of user will be tracked at a specific time of interval(**For Debug 1 Minute otherwise 5 Minutes**) even when App is in background or restarted.
* Synced location - All the tracked location will synced with server after a specific time interval.

## TODO

* Write Unit Tests - This project is written on MVP pattern with dependency injections so writing unit tests for Presenters will be straightforward. This is a next thing to complete in a free time.
* Add functionality of Repeat mode and Day in Alarm - This is a pending task, approach is cleared for this functionalty but yes need some extra time to place this functionalty.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
