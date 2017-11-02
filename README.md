# SSCMasterBackend

This is the data processing back-end for SSC Master Android app.

Running __StoreObjectData__ class will create a single JSON file (`courseList.json`) that contains all of 5898 UBC courses info.
This file is inside the _JsonData_ folder, but the path can be changed by modifying the static defined string value.

* `CourseScheduleManager`: a simple manager system that was built for organizing data
* `DataParser`: a multithreading crawler that iterates over 18,000 course websites on the SSC and parse those data to the management system
* `JsonData`: storing course data in JSON format. 
* `JsonFileStoreAndRead`: helper methods that reads object data and stores JSON files.
