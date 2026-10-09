## Low Level Design of Airport Traffic Control system

### Steps to run locally

- Clone the repo and navigate to the root folder of the repo `cd air-traffic-control`
- Compile the main class Planning.java `javac Planning.java`
- Run the compiled class `java Planning`

----------------

### Sample run:

![Sample Output Image](https://github.com/hd-agarwal/air-traffic-control/blob/master/sample_output.png?raw=true)

-----------


### Requirements:

- A contract is defined as a fixture of an aircraft to a particular terminal for a given duration (the duration is determined by the type of contract, or "haul")
- A ramp is defined as the terminal parking. Each ramp has a predetermined type of haul it can support
- The number of ramps and their haul types are given input by the user.
- Support 2 types of Haul 
    
    - Short Haul (Smaller aircrafts requiring 3 hours ttl)
    - Medium Haul (Medium sized aircrafts requiring 4 hours ttl)
- 1 airstrip common for landing and take off for both haul types
- A ramp can park only one aircraft at a time (no 2 aircraft contracts should overlap)
- Gap between any 2 edge events on any 2 terminals should be configurable and a multiple of 10 minutes

    - An edge event is either of contract start time and contract end time
- A contract's start time minutes and end time minutes should both be multiples of 10
- Goal is to maximise the number of contracts that can be placed within a window of 24 hours
---------------
### ***Contributions are welcome!***
