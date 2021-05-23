; complete documentation at: http://quil.info/api

; ######### SHAPES #########

(q/ellipse x y width height)

(q/rect x y width height)

(q/triangle x1 y1 x2 y2 x3 y3)

(q/point x y)

; ######### LINES #########

(q/line x1 y1 x2 y2)
line(x1 y1 x2 y2)
;

(q/curve control-x1 control-y1 x1 y1 ;; curved line
									x2 y2 control-x2 control-y2)

; ######### SHAPE MODIFIERS #########

(q/fill h s b) ; set fill color of shapes (hue, saturation, brightness)
(q/stroke h s b) ; set color of outlines (hue, saturation, brightness)

(q/stroke-weight value-in-pixels) ; set width of outline

; ######### TEXT #########
(q/text text-content x y)

; ######### RANDOMNESS #########
(q/random lower-limit upper-limit)

; ######### MATH #########
(q/cos radians) ;; calculates cosine ratio
(q/sin radians) ;; same for sine ratio

; ######### REPETITION #########
(doseq [repetition repetitions]
		; code that will be repeated
)

; ######### INTERACTION #########
(q/mouse-x) ; tells where mouse is on X axis
(q/mouse-y) ; tells where mouse is on Y axis

