# StockMarketAssignment

Currently contains 4 project folders:
* TradingClientJava
* TradingServerJava
* TradingClientCS
* TradingServerCS

## Running TradingClientJava and TradingServerJava

Open both of these in IntelliJ. Build both projects, ensuring that on the build toolbar, select
the ClientProgram dropdown box, select Edit Configurations and tick the Allow Parallel Run checkbox.

To execute the server, build and run the file TradingServer.
To execute the client, build and run the file ClientProgram.

## Running TradingClientCS and TradingServerCS

Open both of these in Visual Studio Code. Open a terminal in the folder you wish to execute
and type: `dotnet run`.

## Port and IP Address Configuration

Default values are set to `localhost` and `8888`

* For TradingServerJava: Edit port value in `TradingServer.java`
* For TradingClientJava: Edit port and address values in `Client.java`

* For TradingServerCS: Edit port value in `Program.cs` - for address, edit the listener
constructor parameter `IPAddress.Loopback`
* For TradingClientCS: Edit port and address values in `Client.cs`

## Other Notes

* TradingServerJava can accept clients from both TradingClientJava and TradingClientCS.
* TradingServerCS can accept clients from both TradingClientCS and TradingClientJava.
