(ns clj-rgf93-wgs84.core-test
  (:require [clojure.test :refer :all]
            [clj-rgf93-wgs84.core :refer :all]))

(deftest rumigny
  (testing "Convert Rumigny's coordinates"
    (is (= (rgf93-to-wgs84 791309.3399711377 6968464.420019365)
           [4.26766383214133 49.80844748719485]))))
