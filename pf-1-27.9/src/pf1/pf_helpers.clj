(ns pf1.pf_helpers
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
