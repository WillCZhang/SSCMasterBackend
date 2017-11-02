# SSCMasterBackend

This is the data processing back-end for __SSC Master Android app__.

Running `StoreObjectData` class will create a single JSON file (`courseList.json`) that contains all of 5898 UBC courses info.
This file is inside the `JsonData` folder, but the path can be changed by modifying the static defined string value.

* `CourseObjectManager`: a refactored way of storing course data for `StoreObjectData` class.
* `CourseScheduleManager`: a simple manager system that was built for organizing data.
* `DataParser`: a multithreading crawler that iterates over 18,000 course websites on the SSC and parse those data to the management system.
* `JsonData`: storing course data in JSON format. 
* `JsonFileStoreAndRead`: helper methods that reads object data and stores JSON files.
