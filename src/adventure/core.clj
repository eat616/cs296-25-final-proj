(ns adventure.core
  (require clojure.set)
  (:gen-class))



(def grave-maps
 {:gorgeous-arch {:desc "This is a really gorgeous arch, possibly too gorgeous to be the entrance of a grave."
          :title "under the arch"
          :dir {:east :foyer}
          :contents #{:raw-egg}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "|x| | | | |"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | | |  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  | | | |  "
          :visualize-se "  +-+-+-+  "}
  :foyer {:desc "A very luxurious foyer. We can convey that the owner is some kind of emperor. There's ladder to go down to somewhere."
          :title "in the foyer"
          :dir {:west :gorgeous-arch
                :south :underground-lake}
          :contents #{}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| |x| | | |"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | | |  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  | | | |  "
          :visualize-se "  +-+-+-+  "}
  :underground-lake {:desc "Walking down the floor, we see a lake underground. I can see a huge shadow under the water."
          :title "in front of the lake"
          :dir {:north :foyer
                :south :forest}
          :contents #{:fresh-fish}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| | | | | |"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  |x| | |  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  | | | |  "
          :visualize-se "  +-+-+-+  "}
  :forest {:desc "A weird forest underground. Something is moving inside because I can hear the sound."
          :title "in the forest"
          :dir {:north :underground-lake
                :east :overpalace}
          :contents #{}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| | | | | |"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | | |  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  |x| | |  "
          :visualize-se "  +-+-+-+  "}
  :overpalace {:desc "A palace full of the over"
          :title "in the overpalace"
          :dir {:west :forest
                :east :arena}
          :contents #{}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| | | | | |"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | | |  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  | |x| |  "
          :visualize-se "  +-+-+-+  "}
  :arena {:desc "An ancient arena. Dont know the purpose of building it."
          :title "in the arena"
          :dir {:west :overpalace
                :north :corridor}
          :contents #{}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| | | | | |"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | | |  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  | | |x|  "
          :visualize-se "  +-+-+-+  "}
  :corridor {:desc "A corridor full of rooms. Seems to be the dinning room of slave in the past."
          :title "in the corridor"
          :dir {:south :arena
                :north :throne-room}
          :contents #{}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| | | | | |"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | |x|  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  | | | |  "
          :visualize-se "  +-+-+-+  "}
  :throne-room {:desc "A room of throne."
          :title "in front of the throne"
          :dir {:south :corridor
                :east :buril-hall}
          :contents  #{:rope}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| | | |x| |"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | | |  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  | | | |  "
          :visualize-se "  +-+-+-+  "}
  :buril-hall {:desc "Room full of money."
          :title "in the buril-hall"
          :dir {:west :throne-room}
          :contents #{:ultimate-gem}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| | | | |x|"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | | |  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  | | | |  "
          :visualize-se "  +-+-+-+  "}
})


(def items-list
 {:raw-egg {:desc "This is a raw egg. You probably want to cook it before eating it."
            :name "a raw egg"}
  :fresh-fish {:desc "This is a fresh fish."
            :name "a fresh fish"}
  :baked-fish {:desc "This is a baked fish."
            :name "a baked fish"}
  :ultimate-gem {:desc "The ultimate gem."
            :name "a ultimate gem"}
  :rope {:desc "A rope to escape."
            :name "a rope"}
})


(def init-adventurer
 {:location :gorgeous-arch
  :inventory #{}
  :hp 10
  :lives 3
  :tick 0
  :seen #{}})


(defn new-game []
  {:rooms grave-maps
   :items items-list
   :adventurer init-adventurer})

(defn status [state]
  (let [location (get-in state [:adventurer :location])
        rooms (:rooms state)]
    (do (println (str "You are " (-> rooms location :title) "."))
        (println (-> rooms location :visualize-on))
        (println (-> rooms location :visualize-tw))
        (println (-> rooms location :visualize-th))
        (println (-> rooms location :visualize-fo))
        (println (-> rooms location :visualize-fi))
        (println (-> rooms location :visualize-si))
        (println (-> rooms location :visualize-se)))
    (when-not ((get-in state [:adventurer :seen]) location)
        (println (-> rooms location :desc)))
    (update-in state [:adventurer :seen] #(conj % location))))


(defn go [state dir]
   (let [location (get-in state [:adventurer :location])
         dest ((get-in state [:rooms location :dir]) dir)]
     (if (nil? dest)
       (do (println "You can't go that way.")
           state)
       (assoc-in state [:adventurer :location] dest))))


(defn take-items [state]
   (let [location (get-in state [:adventurer :location])
         exist-items (get-in state [:rooms location :contents])
         all-items (clojure.set/union exist-items (get-in state [:adventurer :inventory]))
         prereturn (assoc-in state [:adventurer :inventory] all-items)]
            (assoc-in prereturn [:rooms location :contents] #{})))

(defn drop-items [state todrop]
   (if (contains? (get-in state [:adventurer :inventory]) todrop)
       (let [settodrop #{todrop}
            current-inventory (get-in state [:adventurer :inventory])
            removed-inventory (into #{} (remove settodrop current-inventory))
            prereturn (assoc-in state [:adventurer :inventory] removed-inventory)
            location (get-in state [:adventurer :location])
            get-contents (clojure.set/union settodrop (get-in state [:rooms location :contents]))]
                (assoc-in prereturn [:rooms location :contents] get-contents))
        (do (println "You do not have such item!")
            state)))


(defn take-single-item [state totake]
   (let [location (get-in state [:adventurer :location])]
      (if (contains? (get-in state [:rooms location :contents]) totake)
          (let [settotake #{totake}
                current-inventory (get-in state [:adventurer :inventory])
                merged-inventory (clojure.set/union settotake current-inventory)
                removed-contents (into #{} (remove settotake (get-in state [:rooms location :contents])))
                prereturn (assoc-in state [:adventurer :inventory] merged-inventory)]
                (assoc-in prereturn [:rooms location :contents] removed-contents))
          (do (println "There's no such item around!")
              state))))


(defn cook [state]
   (if (contains? (get-in state [:adventurer :inventory]) :fresh-fish)
       (let [current-inventory (get-in state [:adventurer :inventory])
             removed-inventory (into #{} (remove #{:fresh-fish} current-inventory))
             cooked-inventory (clojure.set/union #{:baked-fish} removed-inventory)]
              (assoc-in state [:adventurer :inventory] cooked-inventory))
        (do (println "You do not have fresh-fish!")
            state)))

(defn check-escape [state]
    (if (contains? (get-in state [:adventurer :inventory]) :rope)
        (if (= (get-in state [:adventurer :location]) :buril-hall)
             true
             false)
        false))




(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (loop [state (status (new-game))]
    (let [location (get-in state [:adventurer :location])
         rooms (:rooms state)]
         (do
           (println "What do you want to do?")
           (println "[M]ove: move to another room")
           (println "[L]ook: look around")
           (println "[C]heck: check the items around that you can take")
           (println "[Ex]amine: look for the information of an item")
           (println "[I]nventory: check the inventory of my items")
           (println "[T]ake: take items around")
           (println "[Ta]ke-single: take a specific item")
           (println "[D]rop: drop an items in inventory")
           (println "[Co]ok: cook the fish")
           (println "[E]scape: escape from the grave and win the game")
           (println "[Q]uit: quit the game")
         (let [choice (read-line)]
            (cond (= choice "M")
                  (do (println "Which direction (N, S, W, E)")
                      (let [dirchoice (read-line)]
                          (cond (= dirchoice "N")
                            (recur (status (go state :north)))
                                (= dirchoice "S")
                            (recur (status (go state :south)))
                                (= dirchoice "W")
                            (recur (status (go state :west)))
                                (= dirchoice "E")
                            (recur (status (go state :east)))
                                :else
                            (do (println "Invalid input, go back")
                                (recur state)))))
                   (= choice "L")
                   (do (println (get-in rooms [location :desc]))
                       (recur state))
                   (= choice "C")
                   (do (run! println (get-in rooms [location :contents]))
                       (recur state))
                   (= choice "I")
                   (do (run! println (get-in state [:adventurer :inventory]))
                       (recur state))
                   (= choice "T")
                   (do (println "You've taken up all the items around")
                       (recur (take-items state)))
                   (= choice "D")
                   (do (println "Which item you want to drop")
                       (let [dropkey (keyword (read-line))]
                            (recur (drop-items state dropkey))))
                   (= choice "Co")
                       (recur (cook state))
                   (= choice "E")
                       (if (check-escape state)
                          (println "You successfully escape!, thanks for playing the game!")
                          (do (println "You do not meet the condition, keep working !")
                              (recur state)))
                   (= choice "Ex")
                   (do (println "Which item you want to look into?")
                       (let [exkey (keyword (read-line))]
                            (do (println (get-in state [:items exkey :desc]))
                                (recur state))))
                   (= choice "Q")
                       (println "Thanks for playing!")
                   (= choice "Ta")
                   (do (println "Which item you want to take up")
                       (let [takekey (keyword (read-line))]
                            (recur (take-single-item state takekey))))
                   :else
                       (do (println "Invalid input, go back")
                           (recur state))))))))
