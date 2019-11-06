(ns pf.pf_helpers
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def RED [10 100 100])
(def BLUE [5 100 100])

(defn show-xline
  []
  (q/with-stroke RED
    (q/stroke-weight 4)
    (q/line 0 0 (q/mouse-x) 0)))

(defn show-yline
  []
  (q/with-stroke BLUE
    (q/stroke-weight 4)
    (q/line 0 0 0 (q/mouse-y))))

(defn where-is-point
  [x y]
  (q/stroke-weight 0.8)
  (q/with-stroke RED (q/line x y x 0))
  (q/with-stroke BLUE (q/line x y 0 y))
  (q/text-size 18)
  (q/with-fill RED (q/text (str "x " x) (+ x 8) (+ y 4)))
  (q/with-fill BLUE (q/text (str "y " y) (- x 0) (+ y 30)))
  (q/stroke-weight 4)
  (q/point x y)
  )

(defn where-is
  [shape x y & dimensions]
  (if (or (= shape q/point)
          (= shape  q/line)
          (= shape q/ellipse)
          (= shape  q/rect))
    (if-not (nil? dimensions)
      (if (= shape q/point)
          (throw (AssertionError. "Too many arguments problem: q/point needs only 2 arguments, you gave 4"))
          (shape x y (first dimensions) (second dimensions)))
      (shape x y))
      (throw (AssertionError. "Unknown shape: try q/point, q/line, q/ellipse or q/rect")))
  (q/stroke-weight 0.5)
  (q/with-stroke RED (q/line x y x 0))
  (q/with-stroke BLUE (q/line x y 0 y))
  (q/text-size 18)
  (q/with-fill RED (q/text (str "x " x) (+ x 8) (+ y 4)))
  (q/with-fill BLUE (q/text (str "y " y) (- x 0) (+ y 30))))


(defn is-modifier-key? [key-code]
  (let [modifier-keycodes [9 20 16 17 18 157 32 37 40 38 39 10 8 27 112 113 114 115 116 117 118 119 120 121 122 123 144 145]]
        (.contains modifier-keycodes key-code)))
(defn pulse [v reducer]
  (let [pulse (q/sin (* (q/millis) reducer))
        scaled-value (q/abs (* v pulse))]
        scaled-value))

(defn random-point-in-rectangle
  [rectangle]
  (let [{:keys [x y width height]} rectangle
        rect-x-end (+ x width)
        rect-y-end (+ y height)
        new-x (q/random x rect-x-end)
        new-y (q/random y rect-y-end)]
        {:x new-x :y new-y}))

(defn random-point-in-circle
  [width height circle]
  (let [{:keys [x y radius]} circle
        p-x (q/random width)
        p-y (q/random height)
        delta-x (- p-x x)
        delta-y (- p-y y)
        dist2 (+ (* delta-x delta-x) (* delta-y delta-y))
        radius2 (* radius radius)]
      (if (or (= dist2 radius2) (< dist2 radius2))
        {:x p-x :y p-y}
        (recur width height circle))))

