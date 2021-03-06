## The Autosync StackPack is waiting for your action, please send some topology to StackState

The synchronization has been installed.

Next, you can manually test the synchronization to ensure it is working correctly. You can use an HTTP POST request to push topology information to StackState using the [well-defined JSON input format](https://l.stackstate.com/oyJfJn).

## Example POST request

The example below will create a component in StackState with type `application` and name `myDummyApp`. 

1. Save a file named `topology.json` with the following content

```json
{
   "apiKey":"{{config.apiKey}}",
   "internalHostname":"lnx-343242.srv.stackstate.com",
   "topologies":[
      {
         "start_snapshot": false,
         "stop_snapshot": false,
         "instance":{
            "type":"{{configurationConfig.sts_instance_type}}",
            "url":"{{configurationConfig.sts_instance_url}}"
         },
         "components":[
            {
               "externalId":"myDummyApp",
               "type":{
                  "name":"application"
               },
               "data":{
                  "ip_addresses":["172.17.0.8"],
                  "labels":["label1", "category:label2"]
               }
            }
         ],
         "relations":[]
      }
   ]
}
```

2. Run this curl command to push the data to StackState:

``` bash
curl -v user:password -X POST -H "Content-Type: application/json" --data-ascii @topology.json "{{config.baseUrl}}/stsAgent/intake/?api_key={{config.apiKey}}"
```

See the StackState documentation site for a [complete example of the topology JSON format](https://l.stackstate.com/XT73aI).
