(ns pf_oscquil.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [overtone.osc :as osc]))

;; pointfugue meetup6 7.11.19

;; #####
;; #
;; # This sketch needs to run alongside Processing sketch("pf6_processing_fft_amplitudes.pde") included in this package.
;; # Install Minim and oscP5 processing libraries before running this
;; # Procesesing sketch analyses sound with Minim library and then broadcasts the analysis to OSC server.
;; # This sketch uses the volume of sound spectrum segments to distort a grid of rectangles
;; #
;; #####


;
; settings
;


(def WIDTH 500)
(def HEIGHT 500)

(def amount-of-rectangles 16)
(def vertical-gap 4)


;
; utility functions
;

;; calculate an average of collection of values
(defn average-of-collection
  [collection]
  (/ (reduce + collection) (count collection)))

;; partition-spectrum simplifies actual volume spectrum:
;; 1. divide the spectrum into smaller partitions (from 512 to 16 in this sketch)
;; 2. get average of each partition
;; 3. add index to each partition (index from 0,1,2,3 ... to 15)
;; will result in a collection of 16 averages, covering the whole spectrum of sound

(defn partition-spectrum
  [spectrum amount-of-partitions]
  (let
    [ spectrum-size (count spectrum)
      spectrum-partitioned (partition (/ spectrum-size amount-of-partitions) spectrum)
      spectrum-averages (map q/abs (map average-of-collection spectrum-partitioned))
      spectrum-indexed (map vector (range) spectrum-averages)]
    spectrum-indexed)
)



;
; OSC listener setup
;

;; create a variable that will keep sound analysis data
(def p5-analysis-state (atom {}))

; start an OSC server that will handle data from Processing
(def server (osc/osc-server 7000))

; add sound spectrum data to the state variable once they are received
(osc/osc-handle server "/processing/spectrum" (fn [msg] (swap! p5-analysis-state assoc :spectrum (:args msg))))

; add sound volume data to the state variable once they are received
(osc/osc-handle server "/processing/volume" (fn [msg] (swap! p5-analysis-state assoc :volume (:args msg))))


;
; the Quil sketch
;

(defn setup-sketch []
  (q/frame-rate 30)
  (q/color-mode :hsb 100 100 100 100)
  (q/no-stroke)
)
(defn draw-sketch [state]
  (q/background 0 0 10)

  ;; first create the vertices of rectangles and distort them with sound volume
  (let
    ;; spectrum is a collection of sound volume values at different parts of the spectrum
    [spectrum (get @p5-analysis-state :spectrum)
     spectrum-partitioned (partition-spectrum spectrum amount-of-rectangles)
     ;; create 16 rectangles in form of colletion [start-x start-y width height]
     ;; distort the height of each rectangle by a corresponding spectrum volume value
     rectangles (for [spectrum-segment spectrum-partitioned]
                  ;; spectrum-segment is a collection in form of [spectrum-segment-index segment-volume-value]
                     (let
                       [ spectrum-generation (first spectrum-segment) ;; get index
                         spectrum-volume (* (last spectrum-segment) 3) ;; get volume value
                         start-x 0 ;; start of rect
                         start-y (* (/ HEIGHT amount-of-rectangles) spectrum-generation)
                         width WIDTH ;; width of rect
                         height-of-rect (- (/ HEIGHT amount-of-rectangles) vertical-gap) ;; height of rect
                         actual-height (* height-of-rect spectrum-volume) ;; its height multiplied by sound volume
                         height-constrained (q/constrain actual-height 0.5 height-of-rect)] ;; constrain height between 1px and the height of undistorted rect
                       [start-x start-y width height-constrained]))] ;; return the vertices of a rectangle
  ;; second draw the data
    (q/with-fill [3 100 79]
      (doseq [rectangle-points rectangles]
        (apply q/rect rectangle-points))))
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
