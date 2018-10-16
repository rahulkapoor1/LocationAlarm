# LocationAlarm
Alarm App working with Geo Locations

## Getting Started

Idea of this Application is to execute Alarms when user Enter or Exit from Geofence region. This Application includes below mentioned features -

* Create Alarm - User can create new Alarm by enetring Alarm name, Location(for Geofence Trigger), Ringtone for Notification, Vibration enable/disable.
* Update Alarm - User can enable/diable specific Alarm that will also enable/disable the Geofence.
* Track Location - Location of user will be tracked at a specific time of interval(**For Debug 1 Minute otherwise 5 Minutes**) even when App is in background or restarted.
* Synced location - All the tracked location will synced with server after a specific time interval.

## Wiki Page 
Basic overview of code flow.
[https://github.com/rahulkapoor1/LocationAlarm/wiki]

## TODO

* Write Unit Tests - This project is written on MVP pattern with dependency injections so writing unit tests for Presenters will be straightforward. This is a next thing to complete in a free time.
* Add functionality of Repeat mode and Day in Alarm - This is a pending task, approach is cleared for this functionalty but yes need some extra time to place this functionalty.

## License

MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
