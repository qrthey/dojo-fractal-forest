(ns dojo-fractal-forest.geometry)

(defn cos [degr]
  (Math/cos (* (/ degr 180) Math/PI)))

(defn sin [degr]
  (Math/sin (* (/ degr 180) Math/PI)))

(defn endpoint
  [x y angle length]
  [(+ x (* length (cos angle)))
   (+ y (* length (sin angle)))])
