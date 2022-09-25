package de.fhkiel.ki.cathedral.gui;

import java.awt.Color;
import java.awt.Point;
import javax.swing.JLabel;

class Cell {
  final JLabel label;
  final Point position;
  final Color base;
  public Cell(JLabel label, Point position) {
    this.label = label;
    this.position = position;
    this.base = null;
  }

  public Cell(JLabel label, Point position, Color base) {
    this.label = label;
    this.position = position;
    this.base = base;
  }
}
