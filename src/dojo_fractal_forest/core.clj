(ns dojo-fractal-forest.core
  (:gen-class)
  (:require [dojo-fractal-forest.geometry :as g])
  (:import [javax.swing JFrame JPanel JButton JTextField]
           [java.awt BasicStroke]
           [java.awt.event ActionListener]))

(defn adjust-value [val v variation]
  (let [])
  (+ val v (- variation (rand-int (* 2 variation)))))

(defn get-lines [curr-l max-l acc fut]
  (if (< curr-l max-l)
    (recur (inc curr-l) max-l (into acc fut)
           (apply concat
                  (for [[x y angle length width] fut]
                    (let [[x-end y-end] (g/endpoint x y angle length)
                          l (/ length 1.45)
                          w (/ width 1.65)]
                      (if (= 0 (mod  curr-l 2))
                        (rand-nth
                         [[[x-end y-end (adjust-value angle -25 3) l w]
                           [x-end y-end (adjust-value angle 0 10) (/ l 1.05) (/ w 1.85)]
                           [x-end y-end (adjust-value angle 25 3) l w]]
                          [[x-end y-end (adjust-value angle -25 3) l w]
                           [x-end y-end (adjust-value angle 25 3) l w]]])
                        [[x-end y-end (adjust-value angle -25 3) l w]
                         [x-end y-end (adjust-value angle 25 3) l w]])))))
    acc))

(defn draw-line
  [gfx x y angle length width]
  (let [[x-end y-end] (g/endpoint x y angle length)]
    (.setStroke gfx (BasicStroke. width))
    (.drawLine gfx x (- 1000 y) x-end (- 1000 y-end))))

(defn -main
  [& args]
  (let [btn-draw (JButton. "Click To Draw Level")
        btn-clear (JButton. "Clear")
        txt (doto (JTextField. 6)
              (.setText "9"))
        panel (doto (JPanel.)
                (.add txt)
                (.add btn-draw)
                (.add btn-clear))
        frame (doto (JFrame. "Fractals Forest Dojo")
                (.setSize 200 200)
                (.setVisible true)
                (.setContentPane panel))
        click-handler (proxy [ActionListener] []
                        (actionPerformed [event]
                          (let [gfx (.getGraphics panel)
                                growth (+ 0.4 (rand 1.7))
                                level (Integer/parseInt (.getText txt))
                                lines (get-lines 0 level []
                                                 [[(adjust-value 1000.0 0 900)
                                                   (+ 550.0 (* (- 1 growth) 500))
                                                   90.0
                                                   (* growth 80.0)
                                                   (* growth 23.0)]])]
                            (doseq [[x y angle length width] lines]
                              (draw-line gfx x y angle length width)))))
        clear-handler (proxy [ActionListener] []
                            (actionPerformed [event]
                              (.repaint panel)))]
    (.addActionListener btn-draw click-handler)
    (.addActionListener btn-clear clear-handler)))
