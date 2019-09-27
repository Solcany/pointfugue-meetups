;; ns stands for namespace, we basically name our program here
(ns pf1.core
  ;; ':Require' tells the program which libraries other than Clojure itself we want to use
  ;; ':as' tells the program how we want to refer to these libraries when coding
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [pf1.pf_helpers :as pf]))

;; define variables that will set up width and height of the drawing canvas
(def CANVAS-WIDTH 800)
(def CANVAS-HEIGHT 800)



;;;;;;;
;;
;; set-up-sketch runs once when the program is started.
;; It's usually used for settings of the program ( like color mode, font we use etc. )
;;
;;;;;;;

(defn set-up-sketch []
  ;; set color mode of the program, :hsb – hue saturation brigthness
  ;; minimum value is 0
  ;; maximum is 100 ( this can be any other positive number )
  (q/color-mode :hsb 100 100 100)

  ;; q/text-font function tells the program what font we want to use.
  ;; It expects 1 argument, in this case we pass another function as the argument.
  (q/text-font

    ;; create-font makes a font installed in our system usable by Quil
    ;; it expects 3 arguments
    ;; 1: exact name of a font installed on our computer
    ;; 2: font size ( can be changed later )
    ;; 3; font smoothing, true or false
    (q/create-font "Courier new" 16 true))
)



;;;;;;;
;;
;; draw-sketch runs 30 times per second (this can be changed to more or less).
;; From the top to bottom, forever.
;;
;;;;;;;

(defn draw-sketch [data]
;; background fills our canvas with color every time draw is run
;; basically erasing everything that was drawn before
;; it can be given 1 argument which is then applied to hue saturation brightness
;; or 3 different arguments hue saturation brightness
(q/background 10)

;; These two functions visualize X (horizontal) and Y (vertical) coordinate systems of our sketch
;; in relation to position of the mouse on the screen
(pf/show-yline)
(pf/show-xline)

;; q/no-fill tells Quil that shapes drawn after we call it should have no fill
(q/no-fill)

;; q/stroke-weight tells Quil how wide the outline of shapes drawn after we call it should be
(q/stroke-weight 5)

;; q/stroke tells Quil what color the outline should be, like background it expects 1 or 3 arguments.
;; we pass Hue argument as another function q/random
;; this function returns a random number between 0 and 100
(q/stroke (q/random 0 100) 100 100)

;; pf/where-is draws a shape and shows its x & y coordinates
;; it expects 5 arguments
;; 1: the shape we want to draw
;; 2: x (horizontal) coordinate of the shape
;;    In this case we pass another function as X, (q/mouse-x) returns X coordinate of position of the mouse
;; 3: y (vertical) coordinate of the shape
;; 4: the shape's width
;; 5: the shape's height
(pf/where-is
 q/ellipse (q/mouse-x) (q/mouse-y) (q/random 50 200) 200)


 ;; q/translate moves the start of canvas x: 0 y: 0 to different position
 ;; We will get to the topic of translation later on
 (q/translate (q/mouse-x) (q/mouse-y))

 ;; q/rotate rotates the whole canvas around its start x: 0 y: 0
 ;; q/millis returns time since the program was started in milliseconds ( 1s == 1000ms )
 (q/rotate (/ (q/millis) 150))

 ;; Here we create a collection of shapes for pf/where-is is function
 ;; We'll also get to collections later on
 (def shapes [[q/line (- 0 100) 0 100 100] ;; line 1
             [q/line (+ 0 100) 0 100 100] ;; line 2
             [q/line (+ 0 100) -100 250 250] ;; line 3
             [q/line (+ 0 100) 100 250 250] ;; line 4
             [q/ellipse -100 -100 100 100] ;; ellipse 5
             ])

  ;; doseq(do sequence) takes 1 shapes from shapes at a time and 'feeds' it to pf/where-is function
  (doseq [shape shapes]
    ;; q/stroke-weight sets weight of outline of any shapes drawn after it to 10 px
    (q/stroke-weight 10)
      ;; here we 'feed'(apply) arguments of a shape to pf/where-is function
      ;; one shape at a time
      (apply pf/where-is shape))

  ;; when does something when the first argument is true
  ;; in this case the first argument (q/key-pressed?) asks if any button on our keyboard is pressed
  ;; if it true; save a frame of the animation
  (when (q/key-pressed?) (q/save-frame "nice.jpg"))
  )




;; Code below sets up more general settings of our program
;; for example: The size of canvas, which function will be used for setup, which for draw etc.
(q/defsketch pf1
  :title "point fugue meetup 1"
  :size [CANVAS-WIDTH CANVAS-HEIGHT]
  :setup set-up-sketch
  :draw draw-sketch
  :features [:keep-on-top]
  :middleware [m/fun-mode])
