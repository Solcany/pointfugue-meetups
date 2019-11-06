(ns pf.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [pf.pf_helpers :as pf]))

(def WIDTH 600)
(def HEIGHT 800)

;; pointfugue meetup 4 17.10.19

;; do you want to save the generated images to disk?
(def SAVE-IMAGES? false) ; false or true

;; define a collection of colors for sketch
(def colors {:yellow [15 100 100]
             :red [2 100 100]
             :green [20 100 100]
             :dark-grey [20 20 20]})

;; define a collection of shape functions for sketch
(def shapes [q/rect q/ellipse])

;; define the circle in which, all shapes will be drawn
(def circle {:x 300 :y 300 :radius 250})
;; define amount of shapes that we want to draw in the sketch
(def amount-of-shapes 1000)

;; define amount of generations
;; a generation is a single image with circle with shapes drawn in it
(def amount-of-generations 10)

(defn setup-sketch []
  (q/color-mode :hsb 100 100 100)
  (q/rect-mode :center)
  (q/ellipse-mode :center)
  (q/background 15 100 100)
  (q/stroke 15)

  ;; run our generative functions for 'amount-of-generations' times, will result in 10 images
  (doseq [ generation (range 0 amount-of-generations) ]

    ;; draw 'amount-of-shapes' shapes inside the predefined circle
    (doseq [shape (range 0 amount-of-shapes)]
      (let [point-in-circle (pf/random-point-in-circle WIDTH HEIGHT circle) ;; find a point {:x x :y y} in the predefined circle
            names (keys colors) ; get keys (i.e :yellow) of the 'colors' collection
            random-name-of-color (rand-nth names) ; pick a random key from 'names'
            color (get colors random-name-of-color ) ; use the key to get a color value from 'colors' collection
                                                     ; (get function expects arguments: (get collection key-in-collection)
            shape (rand-nth shapes)] ; pick a random drawing function from 'shapes' collection
            (q/fill color)
            (shape (get point-in-circle :x) ;x center
                   (get point-in-circle :y) ;y center
                       (q/random 10 50) ; width
                       (q/random 10 50))) ; height
      )
    (when SAVE-IMAGES? (q/save-frame (str "image-" generation ".jpg")))
    )

  ;; draw the surrounding circle that contains the shapes
  (q/stroke 0)
  (q/no-fill)
  (q/ellipse (get circle :x) ;x center
             (get circle :y) ;y center
             (* (get circle :radius) 2) ;width
             (* (get circle :radius) 2)) ;height


)


(q/defsketch pf
  :title "pointfugue meetup 4"
  :size [WIDTH HEIGHT]
  ; setup function called only once, during sketch initialization.
  :setup setup-sketch

  ; update-state is called on each iteration before draw-state.
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
