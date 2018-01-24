(defproject dojo-fractal-forest "0.1.0-SNAPSHOT"
  :description "Draw some fractal trees in a forest in swing (java.awt)."
  :url "http://github.com/qrthey/dojo-fractal-forest"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot dojo-fractal-forest.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
