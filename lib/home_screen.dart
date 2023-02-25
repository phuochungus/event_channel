import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<StatefulWidget> createState() => HomeScreenState();
}

class HomeScreenState extends State<HomeScreen> {
  static const String EVENT_CHANNEL = "platform_channel_events/connectivity";
  final eventChannel = EventChannel(EVENT_CHANNEL);
  late final networkStream;
  @override
  void initState() {
    super.initState();
    networkStream = eventChannel
        .receiveBroadcastStream()
        .distinct()
        .map((dynamic event) => event as int);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: StreamBuilder<int>(
            stream: networkStream,
            builder: (context, snapshot) {
              switch (snapshot.data) {
                case 0:
                  return const Icon(
                    Icons.signal_wifi_connected_no_internet_4,
                    size: 100,
                  );
                case 1:
                  return const Icon(
                    Icons.wifi,
                    size: 100,
                  );
                case 2:
                  return const Icon(
                    Icons.signal_cellular_4_bar,
                    size: 100,
                  );
                default:
                  return const Text("null");
              }
            }),
      ),
    );
  }
}
