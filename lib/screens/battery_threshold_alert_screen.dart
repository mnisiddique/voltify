import 'package:flutter/material.dart';

class SettingsScreen extends StatefulWidget {
  final int batteryLevel;

  final int thresholdLevel;

  final VoidCallback? onDismiss;

  const SettingsScreen({
    super.key,
    this.batteryLevel = 80,
    this.thresholdLevel = 80,
    this.onDismiss,
  });

  @override
  State<SettingsScreen> createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen>
    with SingleTickerProviderStateMixin {
  late final AnimationController _pulseController;
  late final Animation<double> _pulseAnimation;
  late final Animation<double> _glowAnimation;
  bool _dismissed = false;

  @override
  void initState() {
    super.initState();
    _pulseController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 1800),
    )..repeat(reverse: true);

    _pulseAnimation = Tween<double>(begin: 0.96, end: 1.04).animate(
      CurvedAnimation(parent: _pulseController, curve: Curves.easeInOut),
    );

    _glowAnimation = Tween<double>(begin: 0.25, end: 0.65).animate(
      CurvedAnimation(parent: _pulseController, curve: Curves.easeInOut),
    );
  }

  @override
  void dispose() {
    _pulseController.dispose();
    super.dispose();
  }

  void _handleDismiss() {
    _pulseController.stop();
    if (widget.onDismiss != null) {
      widget.onDismiss!();
    } else {
      setState(() {
        _dismissed = true;
      });
    }
  }

  void _handleReset() {
    setState(() {
      _dismissed = false;
    });
    _pulseController.repeat(reverse: true);
  }

  @override
  Widget build(BuildContext context) {
    final mediaQuery = MediaQuery.of(context);
    final isLandscape = mediaQuery.orientation == Orientation.landscape;

    const bgDark = Color(0xFF0C1017);
    const bgCard = Color(0xFF161E2E);
    const emeraldPrimary = Color(0xFF10B981);
    const amberAccent = Color(0xFFF59E0B);
    const textMuted = Color(0xFF94A3B8);

    return Scaffold(
      backgroundColor: bgDark,
      body: SafeArea(
        child: LayoutBuilder(
          builder: (context, constraints) {
            return AnimatedSwitcher(
              duration: const Duration(milliseconds: 300),
              child: _dismissed
                  ? _buildDismissedView(context, constraints, bgCard, textMuted)
                  : SingleChildScrollView(
                      physics: const ClampingScrollPhysics(),
                      child: ConstrainedBox(
                        constraints: BoxConstraints(
                          minHeight: constraints.maxHeight,
                        ),
                        child: Padding(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 20.0,
                            vertical: 16.0,
                          ),
                          child: isLandscape
                              ? _buildLandscapeLayout(
                                  context,
                                  constraints,
                                  emeraldPrimary,
                                  amberAccent,
                                  bgCard,
                                  textMuted,
                                )
                              : _buildPortraitLayout(
                                  context,
                                  constraints,
                                  emeraldPrimary,
                                  amberAccent,
                                  bgCard,
                                  textMuted,
                                ),
                        ),
                      ),
                    ),
            );
          },
        ),
      ),
    );
  }

  Widget _buildPortraitLayout(
    BuildContext context,
    BoxConstraints constraints,
    Color emeraldPrimary,
    Color amberAccent,
    Color bgCard,
    Color textMuted,
  ) {
    final isCompactHeight = constraints.maxHeight < 640;

    return Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        _buildStatusBadge(amberAccent),

        SizedBox(height: isCompactHeight ? 10 : 16),

        _buildHeroGauge(
          emeraldPrimary,
          amberAccent,
          size: isCompactHeight ? 165.0 : 210.0,
        ),

        SizedBox(height: isCompactHeight ? 12 : 20),

        _buildTextContent(emeraldPrimary, textMuted),

        SizedBox(height: isCompactHeight ? 12 : 20),

        _buildInfoChips(bgCard, emeraldPrimary, amberAccent, textMuted),

        SizedBox(height: isCompactHeight ? 12 : 20),

        _buildActionBanner(amberAccent),

        SizedBox(height: isCompactHeight ? 16 : 24),

        _buildDismissButton(emeraldPrimary),
      ],
    );
  }

  Widget _buildLandscapeLayout(
    BuildContext context,
    BoxConstraints constraints,
    Color emeraldPrimary,
    Color amberAccent,
    Color bgCard,
    Color textMuted,
  ) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Expanded(
          flex: 5,
          child: Center(
            child: _buildHeroGauge(
              emeraldPrimary,
              amberAccent,
              size: 160.0,
              compactTypography: true,
            ),
          ),
        ),
        const SizedBox(width: 20),

        Expanded(
          flex: 6,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Align(
                alignment: Alignment.centerLeft,
                child: _buildStatusBadge(amberAccent),
              ),
              const SizedBox(height: 10),
              _buildTextContent(emeraldPrimary, textMuted, alignLeft: true),
              const SizedBox(height: 14),
              _buildInfoChips(bgCard, emeraldPrimary, amberAccent, textMuted),
              const SizedBox(height: 12),
              _buildActionBanner(amberAccent),
              const SizedBox(height: 14),
              _buildDismissButton(emeraldPrimary),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildStatusBadge(Color amberAccent) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 6),
      decoration: BoxDecoration(
        color: amberAccent.withValues(alpha: 0.15),
        borderRadius: BorderRadius.circular(24),
        border: Border.all(
          color: amberAccent.withValues(alpha: 0.4),
          width: 1.2,
        ),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(Icons.warning_amber_rounded, color: amberAccent, size: 18),
          const SizedBox(width: 8),
          Flexible(
            child: Text(
              'ALERT: CHARGE LIMIT REACHED',
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
              style: TextStyle(
                color: amberAccent,
                fontSize: 12,
                fontWeight: FontWeight.w700,
                letterSpacing: 0.8,
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildHeroGauge(
    Color emeraldPrimary,
    Color amberAccent, {
    required double size,
    bool compactTypography = false,
  }) {
    return AnimatedBuilder(
      animation: _pulseController,
      builder: (context, child) {
        return Transform.scale(
          scale: _pulseAnimation.value,
          child: Stack(
            alignment: Alignment.center,
            children: [
              Container(
                width: size + 20,
                height: size + 20,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  boxShadow: [
                    BoxShadow(
                      color: emeraldPrimary.withValues(
                        alpha: _glowAnimation.value * 0.4,
                      ),
                      blurRadius: 36,
                      spreadRadius: 6,
                    ),
                    BoxShadow(
                      color: amberAccent.withValues(
                        alpha: _glowAnimation.value * 0.22,
                      ),
                      blurRadius: 18,
                      spreadRadius: 2,
                    ),
                  ],
                ),
              ),

              SizedBox(
                width: size,
                height: size,
                child: CircularProgressIndicator(
                  value: (widget.batteryLevel / 100.0).clamp(0.0, 1.0),
                  strokeWidth: compactTypography ? 9 : 11,
                  backgroundColor: const Color(0xFF1E293B),
                  valueColor: AlwaysStoppedAnimation<Color>(emeraldPrimary),
                  strokeCap: StrokeCap.round,
                ),
              ),

              Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Icon(
                    Icons.power_settings_new_rounded,
                    color: amberAccent,
                    size: compactTypography ? 24 : 30,
                  ),
                  const SizedBox(height: 2),
                  Row(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        '${widget.batteryLevel}',
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: compactTypography ? 42 : 52,
                          fontWeight: FontWeight.w800,
                          height: 1.0,
                          letterSpacing: -1.5,
                        ),
                      ),
                      Text(
                        '%',
                        style: TextStyle(
                          color: emeraldPrimary,
                          fontSize: compactTypography ? 18 : 22,
                          fontWeight: FontWeight.w700,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 2),
                  Text(
                    'CHARGED',
                    style: TextStyle(
                      color: textMutedColor,
                      fontSize: compactTypography ? 9.5 : 11,
                      fontWeight: FontWeight.w700,
                      letterSpacing: 2.0,
                    ),
                  ),
                ],
              ),
            ],
          ),
        );
      },
    );
  }

  static const textMutedColor = Color(0xFF94A3B8);

  Widget _buildTextContent(
    Color emeraldPrimary,
    Color textMuted, {
    bool alignLeft = false,
  }) {
    return Column(
      crossAxisAlignment: alignLeft
          ? CrossAxisAlignment.start
          : CrossAxisAlignment.center,
      children: [
        Text(
          'Battery Threshold Reached',
          textAlign: alignLeft ? TextAlign.left : TextAlign.center,
          style: const TextStyle(
            color: Colors.white,
            fontSize: 22,
            fontWeight: FontWeight.w800,
            letterSpacing: -0.5,
          ),
        ),
        const SizedBox(height: 6),
        Text(
          'Battery has reached your target of ${widget.thresholdLevel}%. '
          'Please unplug the charger to protect long-term battery health.',
          textAlign: alignLeft ? TextAlign.left : TextAlign.center,
          style: TextStyle(
            color: textMuted,
            fontSize: 13.5,
            height: 1.4,
            fontWeight: FontWeight.w400,
          ),
        ),
      ],
    );
  }

  Widget _buildInfoChips(
    Color bgCard,
    Color emeraldPrimary,
    Color amberAccent,
    Color textMuted,
  ) {
    return Row(
      children: [
        Expanded(
          child: _buildMetricTile(
            icon: Icons.battery_charging_full_rounded,
            iconColor: emeraldPrimary,
            label: 'CURRENT LEVEL',
            value: '${widget.batteryLevel}%',
            bgCard: bgCard,
            textMuted: textMuted,
          ),
        ),
        const SizedBox(width: 10),
        Expanded(
          child: _buildMetricTile(
            icon: Icons.flag_rounded,
            iconColor: amberAccent,
            label: 'SET THRESHOLD',
            value: '${widget.thresholdLevel}%',
            bgCard: bgCard,
            textMuted: textMuted,
          ),
        ),
      ],
    );
  }

  Widget _buildMetricTile({
    required IconData icon,
    required Color iconColor,
    required String label,
    required String value,
    required Color bgCard,
    required Color textMuted,
  }) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 10),
      decoration: BoxDecoration(
        color: bgCard,
        borderRadius: BorderRadius.circular(14),
        border: Border.all(color: const Color(0xFF263348), width: 1),
      ),
      child: Row(
        children: [
          Container(
            padding: const EdgeInsets.all(6),
            decoration: BoxDecoration(
              color: iconColor.withValues(alpha: 0.12),
              shape: BoxShape.circle,
            ),
            child: Icon(icon, color: iconColor, size: 18),
          ),
          const SizedBox(width: 8),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  label,
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(
                    color: textMuted,
                    fontSize: 9.0,
                    fontWeight: FontWeight.w600,
                    letterSpacing: 0.3,
                  ),
                ),
                const SizedBox(height: 2),
                Text(
                  value,
                  style: const TextStyle(
                    color: Colors.white,
                    fontSize: 16,
                    fontWeight: FontWeight.w700,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildActionBanner(Color amberAccent) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 11),
      decoration: BoxDecoration(
        color: amberAccent.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(14),
        border: Border.all(color: amberAccent.withValues(alpha: 0.3), width: 1),
      ),
      child: Row(
        children: [
          Icon(Icons.power_off_rounded, color: amberAccent, size: 22),
          const SizedBox(width: 10),
          Expanded(
            child: Text(
              'Unplug the charger now to complete charging cycle.',
              style: TextStyle(
                color: amberAccent,
                fontSize: 12.5,
                fontWeight: FontWeight.w600,
                height: 1.3,
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildDismissButton(Color emeraldPrimary) {
    return SizedBox(
      width: double.infinity,
      height: 52,
      child: ElevatedButton.icon(
        onPressed: _handleDismiss,
        icon: const Icon(Icons.check_circle_outline_rounded, size: 20),
        label: const Text(
          'Dismiss Alert',
          style: TextStyle(
            fontSize: 15,
            fontWeight: FontWeight.w800,
            letterSpacing: 0.3,
          ),
        ),
        style: ElevatedButton.styleFrom(
          backgroundColor: emeraldPrimary,
          foregroundColor: const Color(0xFF042F22),
          padding: const EdgeInsets.symmetric(horizontal: 16),
          elevation: 4,
          shadowColor: emeraldPrimary.withValues(alpha: 0.5),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(16),
          ),
        ),
      ),
    );
  }

  Widget _buildDismissedView(
    BuildContext context,
    BoxConstraints constraints,
    Color bgCard,
    Color textMuted,
  ) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(28.0),
        child: Container(
          padding: const EdgeInsets.all(24.0),
          decoration: BoxDecoration(
            color: bgCard,
            borderRadius: BorderRadius.circular(24),
            border: Border.all(color: const Color(0xFF263348), width: 1),
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Container(
                padding: const EdgeInsets.all(16),
                decoration: BoxDecoration(
                  color: const Color(0xFF10B981).withValues(alpha: 0.15),
                  shape: BoxShape.circle,
                ),
                child: const Icon(
                  Icons.check_circle_rounded,
                  color: Color(0xFF10B981),
                  size: 48,
                ),
              ),
              const SizedBox(height: 18),
              const Text(
                'Alert Dismissed',
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 20,
                  fontWeight: FontWeight.w700,
                ),
              ),
              const SizedBox(height: 8),
              Text(
                'Remember to disconnect your charger to avoid unnecessary wear on your battery.',
                textAlign: TextAlign.center,
                style: TextStyle(color: textMuted, fontSize: 14, height: 1.4),
              ),
              const SizedBox(height: 24),
              OutlinedButton.icon(
                onPressed: _handleReset,
                icon: const Icon(Icons.refresh_rounded, size: 18),
                label: const Text('Preview Again'),
                style: OutlinedButton.styleFrom(
                  foregroundColor: Colors.white,
                  side: const BorderSide(color: Color(0xFF334155)),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
