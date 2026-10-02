class Settings {
  final int threshold;
  final String speech;
  final String languageModel;
  final int delay;
  final bool isAlertOn;

  const Settings({
    required this.threshold,
    required this.speech,
    required this.languageModel,
    required this.delay,
    required this.isAlertOn,
  });

  factory Settings.fromMap(Map<String, dynamic> json) {
    return Settings(
      threshold: json['threshold'] as int,
      speech: json['speech'] as String,
      languageModel: json['languageModel'] as String,
      delay: json['delay'] as int,
      isAlertOn: json['isAlertOn'] as bool,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'threshold': threshold,
      'speech': speech,
      'languageModel': languageModel,
      'delay': delay,
      'isAlertOn': isAlertOn,
    };
  }

  // Helpful for updating specific fields immutably in state management
  Settings copyWith({
    int? threshold,
    String? speech,
    String? languageModel,
    int? delay,
    bool? isAlertOn,
  }) {
    return Settings(
      threshold: threshold ?? this.threshold,
      speech: speech ?? this.speech,
      languageModel: languageModel ?? this.languageModel,
      delay: delay ?? this.delay,
      isAlertOn: isAlertOn ?? this.isAlertOn,
    );
  }
}