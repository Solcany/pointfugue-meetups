(ns pf.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [pf.pf_helpers :as pf]))

(def WIDTH 600)
(def HEIGHT 800)

(def generations 10)

(def font-names ["Arnhem-Bold"
                 "Auto2-BoldItalic"
                 "Quadraat-Regular"])

(def dada ["Stopping have before to must dark"
           "Woods the village with of horse not"
           "Though and wind between stop queer"
           "If mistake darkest"])

(def shapes [ q/rect q/ellipse ])

(def colors [ {:yellow [15 100 80]
               :redish [3 100 80]
               :brown [18 100 40] }

              {:green [30 100 80]
               :greenish [25 100 80]
               :greenest [35 100 80]}

              {:blue [65 100 80]
               :blueish [60 100 80]
               :white [100 100 100]} ]
              )


(def bgs {:pale-yellow [15 15 70]
          :red [3 70 80]
          :yellow [15 100 90]})

(def parent-circle {:x 300
                    :y 350
                    :radius 250})

(def children-rects-amount 200)

(def children-rects-width {:min 25
                           :max 50})

(def children-rects-height {:min 25
                            :max 100})

(defn setup-sketch []
  (q/frame-rate 30)
  (q/color-mode :hsb 100 100 100)
  (q/rect-mode :center)
  (q/ellipse-mode :center)
  (q/text-align :center)

  (def fonts {:font1 (q/create-font (nth font-names 0) 16 true)
              :font2 (q/create-font (nth font-names 1) 16 true)
              :font3 (q/create-font (nth font-names 2) 16 true)})

  (q/stroke 20)


  (doseq [generation (range generations)]
    (q/text-font ((rand-nth (keys fonts)) fonts))
    (let [ palette (rand-nth colors)
           shape (rand-nth shapes)]
    (apply q/background ((rand-nth (keys bgs)) bgs))
    (doseq [s (range children-rects-amount)]
            (let [
                  shape-center (pf/random-point-in-circle WIDTH HEIGHT parent-circle)
                  shape-width (q/random (:min children-rects-width)
                                       (:max children-rects-height))
                  shape-height (q/random (:min children-rects-width)
                                        (:max children-rects-height))
                  color ((rand-nth (keys palette)) palette)
                  depth (q/round (q/random 1 5))]
                  (q/with-fill color
                    (shape (:x shape-center)
                           (:y shape-center)
                           shape-width
                           shape-height)
                    (doseq [d (range 1 depth)]
                      (let [delta (* d 10)
                            color-delta (* d 3)]
                         (q/with-fill (map #(- % color-delta) color)
                          (shape (:x shape-center)
                                 (:y shape-center)
                                 (- shape-width delta)
                                 (- shape-height delta)
                           ))))))))

    (q/with-fill 0
    (q/text-size 30)
    (q/text (rand-nth dada) (/ WIDTH 2) 700))
    (let [timestamp (+ (q/millis) (rand-int 100))]
    (q/save-frame (str "generation-" generation ".jpg")))))



(q/defsketch pf
  :title "pointfugue meetup template"
  :size [WIDTH HEIGHT]
  ; setup function called only once, during sketch initialization.
  :setup setup-sketch
  ; update-state is called on each iteration before draw-state.
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
