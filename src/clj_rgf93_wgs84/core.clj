(ns clj-rgf93-wgs84.core)

;; Lambert 93 constants

(def ^{:private true} n   0.7256077650)
(def ^{:private true} C   11754255.426)
(def ^{:private true} Xs  700000.0)
(def ^{:private true} Ys  12655612.050)
(def ^{:private true} e   0.08181919112)
(def ^{:private true} l0  (* 3 (/ Math/PI 180.0)))
(def ^{:private true} eps 1e-10)

;;; Core conversion functions

;; (Math/sqrt (+ (Math/pow (- x Xs) 2)
;;               (Math/pow (- y Ys) 2)))
(defn- R [x y] (Math/hypot (- x Xs) (- y Ys))))

(defn- gamma [x y]
  (Math/atan
   (/ (- x Xs)
      (- Ys y))))

(defn- pound [x y]
  (* (/ -1.0 n)
     (Math/log (Math/abs (/ (R x y) C)))))

(defn- lat-from-latISO [latiso e]
  (let [phi0 (- (* 2.0 (Math/atan (Math/exp latiso)))
                (/ Math/PI 2.0))
	phii #(- (* 2.0 (Math/atan
                         (* (Math/pow (/ (inc (* e (Math/sin %)))
                                         (- 1 (* e (Math/sin %))))
                                      (/ e 2.0))
                            (Math/exp latiso))))
                 (/ Math/PI 2.0))]
    (loop [start phi0
           phi1 (phii start)]
      (if (< (Math/abs (- phi1 start)) eps)
        phi1
        (recur phi1 (phii phi1))))))

;;; Output

;; GPS Longitude
(defn- lambda [x y]
  (Math/toDegrees (+ l0 (/ (gamma x y) n))))

;; GPS Latitude
(defn- phi [x y]
  (Math/toDegrees (lat-from-latISO (pound x y) e)))

;;; Public function
(defn rgf93-to-wgs84
  "Convert RGF93 coordinates into WGS84 coordinates."
  [x y]
  (vector (lambda x y) (phi x y)))
