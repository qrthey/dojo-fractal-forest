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
  [gfx x y angle length width screen-height]
  (let [[x-end y-end] (g/endpoint x y angle length)]
    (.setStroke gfx (BasicStroke. (/ width 2)))
    (.drawLine gfx x (- screen-height y) x-end (- screen-height y-end))))

(defn get-methods
  [x]
  (map #(.getName %)
       (.getMethods (type x))))

(defn frame-size
  [frame]
  (let [size (.size frame)]
    {:width (.getWidth size)
     :height (.getHeight size)}))

(defn haha-info [frame]
  (frame-size frame))

(defn -main
  [& args]
  (let [btn-draw (JButton. "To Draw Level")
        btn-clear (JButton. "Clear")
        btn-show-value (JButton. "Show Value")
        txt (doto (JTextField. 6)
              (.setText "9"))
        panel (doto (JPanel.)
                (.add txt)
                (.add btn-draw)
                (.add btn-clear)
                (.add btn-show-value))        
        frame (doto (JFrame. "Fractals Forest Dojo")
                (.setSize 200 200)
                (.setVisible true)
                (.setContentPane panel))
        click-handler (proxy [ActionListener] []
                        (actionPerformed [event]
                          (let [gfx (.getGraphics panel)
                                growth (+ 0.4 (rand 1.7))
                                level (Integer/parseInt (.getText txt))
                                {:keys [width height]} (frame-size frame)
                                lines (get-lines 0 level []
                                                 [[(adjust-value (/ width 2) 0 (/ width 2))
                                                   (/ height growth 5)
                                                   90.0
                                                   (* growth 80.0)
                                                   (* growth 33.0)]])]
                            (doseq [[x y angle length width] lines]
                              (draw-line gfx x y angle length width height)))))
        clear-handler (proxy [ActionListener] []
                            (actionPerformed [event]
                              (.repaint panel)))]
    (.addActionListener btn-draw click-handler)
    (.addActionListener btn-show-value (proxy [ActionListener] []
                                         (actionPerformed [event]
                                           (clojure.pprint/pprint
                                            (haha-info frame)))))
    (.addActionListener btn-clear clear-handler)))
