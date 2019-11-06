(ns pf_oscquil.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [overtone.osc :as osc]))

(def WIDTH 600)
(def HEIGHT 600)

;; pointfugue meetup 4 17.10.19

;; #####
;; #
;; # This sketch needs to run alongside Processing sketch("pf5_Processing_sound_analysis.pde") included in this package.
;; # Procesesing sketch analyses sound with Minim library and then broadcasts the analysis to OSC server.
;; # This sketch then listens for data on the server and uses them to distort and draw a polygon.
;; #
;; #####


;; define a collection of vertices that we will be distorting with microphone input
;; [ [x y] [x y] [x y] ... ]
(def POLYGON
  [
    [100 100]
    [300 50]
    [500 100]
    [500 300]
    [400 500]
    [100 500]
  ]
)
;; define minimum and maxium value to distort a vertex's x and y values
(def VERTEX-DISTORTION-MINMAX
  {
    :min -25
    :max 25
  })

;; create a variable that will keep sound analysis data
(def p5-analysis-state (atom {}))

; start an OSC server that will handle data from Processing
(def server (osc/osc-server 7000))

; add sound spectrum data to the state variable once they are received
(osc/osc-handle server "/processing/spectrum" (fn [msg] (swap! p5-analysis-state assoc :spectrum (:args msg))))

; add sound volume data to the state variable once they are received
(osc/osc-handle server "/processing/volume" (fn [msg] (swap! p5-analysis-state assoc :volume (:args msg))))

;; add a single value to a vertex [x y]
;; what it does:
;; 1. takes x out of vertex adds the value to it
;; 2. takes y out of vertex add the value to it
;; 3. returns [x+value y+value]
(defn add-to-vertex
  ;; this function expects vertex in form of [x y]
  ;; value is a single value to be added, i.e 10, 1.1, 42 and so on
  [vertex value]
  (map #(+ value %) vertex))


; the sketch
(defn setup-sketch []
  (q/frame-rate 30)
  (q/color-mode :hsb 100 100 100 100)
)

(defn draw-sketch [state]
  (q/no-stroke)

  (q/with-fill [0 0 0 20]
    (q/rect 0 0 WIDTH HEIGHT))

  (q/no-fill)
  (let
    [volume (first (get @p5-analysis-state :volume)) ;; get microphone volume from Processing analysis
     volume-bigger (* volume 100);; scale up this value by multiplying it by 100 ( a totally arbitrary value )
     hue (* volume-bigger 100) ]
    (q/stroke hue 100 100)
    ;; start drawing a polygon shape
    (q/begin-shape)
    (q/stroke-weight (q/constrain (* 10 volume-bigger) 1 20)) ;; affect stroke weight with microphone
    (doseq [vertex POLYGON] ;; draw the polygon, one vertex at a time

      (let [ distortion-value (* (q/random (get VERTEX-DISTORTION-MINMAX :min)
                                           (get VERTEX-DISTORTION-MINMAX :max)) volume-bigger)
             distorted-vertex (add-to-vertex vertex distortion-value)]
      ;; apply [x y] values of distorted-vertex one by one to q/vertex function
      (apply q/vertex distorted-vertex)))
    (q/end-shape :close)) ;; finish drawing
)



(q/defsketch pf_oscquil
  :title ""
  :size [WIDTH HEIGHT]
  ; setup function called only once, during sketch initialization.
  :setup setup-sketch
  ; update-state is called on each iteration before draw-state.
 ; :update update-sketch
  :draw draw-sketch
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
