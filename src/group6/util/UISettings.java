package group6.util;

import java.awt.*;

public class UISettings {
    public static final int VIEW_HEIGHT = 200;
    public static final int VIEW_WIDTH = 450;

    public static final Point GateConsolePosition = new Point(0 + VIEW_WIDTH, 30);
    public static final Point LATCPosition = new Point(0 + VIEW_WIDTH, 30 + VIEW_HEIGHT);
    public static final Point GOCPosition = new Point(0 + VIEW_WIDTH, 30 + VIEW_HEIGHT * 2);
    public static final Point CleaningSupervisorPosition = new Point(0, 30);
    public static final Point MaintananceInspectorPosition = new Point(0, 30 + VIEW_HEIGHT);
    public static final Point RefuelingSupervisorPosition = new Point(0, 30 + VIEW_HEIGHT * 2);
    public static final Point RadarTransceiverPosition = new Point(0 + VIEW_WIDTH * 2, 30);
    public static final Rectangle RadarTransceiverBound = new Rectangle(0, 30, 729, 452);
}