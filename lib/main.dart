// ignore_for_file: use_key_in_widget_constructors

import 'package:flutter/material.dart';

import 'home_screen.dart';

void main() {
  runApp(MainApp());
}

class MainApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: HomeScreen(),
    );
  }
}
