# Scaling Mobs
This is a server side mod allows for scaling of the attributes health, damage and speed. You can configure it through a file and choose on what days The attributes should be applied.

Here's a list of the configuration options that can be found in the `scalingmobs.toml` file within the configuration directory.
| Name                | Value                      | Description                                                                        |
| ------------------- | -------------------------- | ---------------------------------------------------------------------------------- |
| activeNthDay        | Number                     | A number of 3 means that the attributes will be applied every 3rd day              |
| start               | Number                     | The start value of scaling                                                         |
| max                 | Number                     | The maximum value it can scale to                                                  |
| min                 | Number                     | The minimum value it can scale to                                                  |
| scalingType         | "LINEAR","EXPONENTIAL"     | Should it scale linerly or exponentially                                           |
| exponentialIncrease | Number (Recommend 1.0-2.0) | If scalingType is exponential then what procentual increase should it have per day |
| linearIncrease      | Number                     | If scalingType is linear then what value increase should it have per day           |
| scaleByActiveDays   | true,false                 | Should it scale by days passed or active days passed                               |
