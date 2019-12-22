(ns adventure.core
  (require clojure.set)
  (:gen-class))



(def grave-maps
 {:gorgeous-arch {:desc "The entrance of a huge palace that seems to exist since ancient time. But how is it possible to build such a palace underground? How can I escape?"
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
  :foyer {:desc "A very luxurious and majestic foyer. The owner must be some kind of emperor in the past."
          :title "in the foyer"
          :dir {:west :gorgeous-arch
                :south :underground-lake}
          :contents #{:hat}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| |x| | | |"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | | |  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  | | | |  "
          :visualize-se "  +-+-+-+  "}
  :underground-lake {:desc "An underground lake. Something is moving fastly inside."
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
          :contents #{:shoes}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| | | | | |"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | | |  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  |x| | |  "
          :visualize-se "  +-+-+-+  "}
  :overpalace {:desc "A room full of skulls. Maybe the palace is actually a grave."
          :title "in the skulls' room"
          :dir {:west :forest
                :east :arena}
          :contents #{:skullhead}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| | | | | |"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | | |  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  | |x| |  "
          :visualize-se "  +-+-+-+  "}
  :arena {:desc "An ancient arena inside the palace. As uncanny as the palace itself."
          :title "in the arena"
          :dir {:west :overpalace
                :north :corridor}
          :contents #{:chest}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| | | | | |"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | | |  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  | | |x|  "
          :visualize-se "  +-+-+-+  "}
  :corridor {:desc "A corridor with rooms on both sides. People living in them in ancient time. Possibly they are the rooms for maid."
          :title "in the corridor"
          :dir {:south :arena
                :north :throne-room}
          :contents #{:triangle-shape-key}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| | | | | |"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | |x|  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  | | | |  "
          :visualize-se "  +-+-+-+  "}
  :throne-room {:desc "Cross the corridor I see the throne. This should be the center of the palace."
          :title "in front of the throne"
          :dir {:south :corridor
                :east :buril-hall}
          :contents  #{:square-shape-key}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| | | |x| |"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | | |  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  | | | |  "
          :visualize-se "  +-+-+-+  "}
  :buril-hall {:desc "A tunnel made by grave robber. I can hear people talking outside. Use a rope can possibly get me out of here."
          :title "in the tunnel"
          :dir {:west :throne-room}
          :contents #{:flawed-gem
                      :ring-shape-key}
          :visualize-on "+-+-+ +-+-+"
          :visualize-tw "| | | | |x|"
          :visualize-th "+-+-+ +-+-+"
          :visualize-fo "  | | | |  "
          :visualize-fi "  +-+-+-+  "
          :visualize-si "  | | | |  "
          :visualize-se "  +-+-+-+  "}
})


(def items-list
 {:raw-egg {:desc "This is a raw egg. You probably want to cook it before eating it, or you will be hurt."
            :name "a raw egg"}
  :baked-egg {:desc "This is a baked egg. It can be eaten to make you energetic again."
            :name "a baked egg"}
  :fresh-fish {:desc "This is a fresh fish. You probably want to cook it before eating it, or you will be hurt."
            :name "a fresh fish"}
  :baked-fish {:desc "This is a baked fish. It can be eaten to make you energetic again."
            :name "a baked fish"}
  :ultimate-gem {:desc "This is the ultimate gem. It contains a huge amount of magic power"
            :name "a ultimate gem"}
  :rope {:desc "This is a long rope. You can probably use it to escape from here."
            :name "a rope"}
  :chest {:desc "A chest from ancient time. Nobody knows what's inside. There is a ring-shape keyhole on it, a key in this shape might help."
            :name "a chest"}
  :ring-shape-key {:desc "This is a key that looks like a ring. It can be used to unlock something."
            :name "a ring shape key"}
  :square-shape-key {:desc "This is a key in square shape. It can be used to unlock something."
            :name "a square shape key"}
  :flawed-gem {:desc "This is a gem with flaw. Magic power are leaking out of it."
            :name "a flawed gem"}
  :triangle-shape-key {:desc "This is a key in triangle shape. It can be used to unlock something."
            :name "a triangle shape key"}
  :hat {:desc "This is a hat. It should be really cheap. So it might be left by some grave robbers."
            :name "a hat"}
  :shoes {:desc "This is a pair of shoes. It should be really cheap. So it might be left by some grave robbers."
            :name "a pair of shoes"}
  :skullhead {:desc "This is a skullhead. Its owner died thousands of years ago."
            :name "a skullhead"}
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
    (do (println " ")
        (println (str "You are " (-> rooms location :title) "."))
        (println " ")
        (println "your current location:")
        (println " ")
        (println (-> rooms location :visualize-on))
        (println (-> rooms location :visualize-tw))
        (println (-> rooms location :visualize-th))
        (println (-> rooms location :visualize-fo))
        (println (-> rooms location :visualize-fi))
        (println (-> rooms location :visualize-si))
        (println (-> rooms location :visualize-se))
        (println " "))
    (when-not ((get-in state [:adventurer :seen]) location)
        (do (println (-> rooms location :desc))
            (println " ")))
    (update-in state [:adventurer :seen] #(conj % location))))


(defn go [state dir]
   (let [location (get-in state [:adventurer :location])
         dest ((get-in state [:rooms location :dir]) dir)]
     (if (nil? dest)
       (do (println " ")
           (println "You can't go that way.")
           (println " ")
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
       (do (println " ")
           (println "Successfully drop the item!")
           (println " ")
           (let [settodrop #{todrop}
            current-inventory (get-in state [:adventurer :inventory])
            removed-inventory (into #{} (remove settodrop current-inventory))
            prereturn (assoc-in state [:adventurer :inventory] removed-inventory)
            location (get-in state [:adventurer :location])
            get-contents (clojure.set/union settodrop (get-in state [:rooms location :contents]))]
                (assoc-in prereturn [:rooms location :contents] get-contents)))
        (do (println " ")
            (println "You do not have such item!")
            (println " ")
            state)))


(defn take-single-item [state totake]
   (let [location (get-in state [:adventurer :location])]
      (if (contains? (get-in state [:rooms location :contents]) totake)
          (do (println " ")
              (println "Successfully picked up the item!")
              (println " ")
              (let [settotake #{totake}
                current-inventory (get-in state [:adventurer :inventory])
                merged-inventory (clojure.set/union settotake current-inventory)
                removed-contents (into #{} (remove settotake (get-in state [:rooms location :contents])))
                prereturn (assoc-in state [:adventurer :inventory] merged-inventory)]
                (assoc-in prereturn [:rooms location :contents] removed-contents)))
          (do (println " ")
              (println "There's no such item around!")
              (println " ")
              state))))


(defn cook-fish [state]
   (if (contains? (get-in state [:adventurer :inventory]) :fresh-fish)
       (do (println " ")
           (println "fresh fish now becomes baked fish!")
           (println " ")
           (let [current-inventory (get-in state [:adventurer :inventory])
             removed-inventory (into #{} (remove #{:fresh-fish} current-inventory))
             cooked-inventory (clojure.set/union #{:baked-fish} removed-inventory)]
              (assoc-in state [:adventurer :inventory] cooked-inventory)))
        (do (println " ")
            (println "You do not have fresh-fish!")
            (println " ")
            state)))

(defn cook-egg [state]
   (if (contains? (get-in state [:adventurer :inventory]) :raw-egg)
       (do (println " ")
           (println "raw egg now becomes baked egg!")
           (println " ")
           (let [current-inventory (get-in state [:adventurer :inventory])
             removed-inventory (into #{} (remove #{:raw-egg} current-inventory))
             cooked-inventory (clojure.set/union #{:baked-egg} removed-inventory)]
              (assoc-in state [:adventurer :inventory] cooked-inventory)))
       (do (println " ")
           (println "You do not have raw egg!")
           (println " ")
           state)))

(defn check-escape [state]
    (if (contains? (get-in state [:adventurer :inventory]) :rope)
        (if (= (get-in state [:adventurer :location]) :buril-hall)
             true
             false)
        false))

(defn unlock-chest [state]
    (let [current-inventory (get-in state [:adventurer :inventory])]
        (if (contains? current-inventory :chest)
            (if (contains? current-inventory :ring-shape-key)
                (let [remove-key (into #{} (remove #{:ring-shape-key} current-inventory))
                      remove-key-chest (into #{} (remove #{:chest} remove-key))
                      add-rope (clojure.set/union #{:rope} remove-key-chest)]
                        (do (println " ")
                            (println "Unlock successfully! You now have a rope! Seems like it can be used to escape")
                            (println " ")
                            (assoc-in state [:adventurer :inventory] add-rope)))
                (do (println " ")
                    (println "You do not have the right key!")
                    (println " ")
                    state))
            (do (println " ")
                (println "You do not have the chest!")
                (println " ")
                state))))


(defn eat [state toeat]
  (let [current-inventory (get-in state [:adventurer :inventory])]
      (if (contains? current-inventory toeat)
          (cond (or (= toeat :baked-egg) (= toeat :baked-fish))
                (do (println " ")
                    (println "it's delicous, you feel energetic!")
                    (println " ")
                    (let [current-hp (get-in state [:adventurer :hp])
                          hp-added (assoc-in state [:adventurer :hp] (+ current-hp 5))
                          remove-eat (into #{} (remove #{toeat} current-inventory))
                          to-return (assoc-in hp-added [:adventurer :inventory] remove-eat)]
                            to-return))
                :else
                (do (println " ")
                    (println "You shouldn't eat this!!!!")
                    (println " ")
                    (let [current-hp (get-in state [:adventurer :hp])
                          current-lives (get-in state [:adventurer :lives])
                          decreased-hp (- current-hp 6)
                          decreased-hpt (if (< decreased-hp 0) 10 decreased-hp)
                          decreased-lives (if (< decreased-hp 0) (- current-lives 1) current-lives)
                          decreased-hp-return (assoc-in state [:adventurer :hp] decreased-hpt)
                          decreased-lives-return (assoc-in decreased-hp-return [:adventurer :lives] decreased-lives)
                          remove-eat (into #{} (remove #{toeat} current-inventory))
                          to-return (assoc-in decreased-lives-return [:adventurer :inventory] remove-eat)]
                              (if (< decreased-lives 0)
                                  1
                                  to-return))))
           (do (println " ")
               (println "You do not have such item!")
               (println " ")
               state))))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (loop [state (status (new-game))]
    (let [location (get-in state [:adventurer :location])
         rooms (:rooms state)]
         (do
           (println "What do you want to do?")
           (println "-move: move to another room")
           (println "-look: look around")
           (println "-check: check the items around that you can take")
           (println "-lifestat: check your life stat (# of lives, hp)")
           (println "-examine: show information of items (example: to examine raw-egg, just type in raw-egg)")
           (println "-inventory: check the inventory")
           (println "-take: take all the items around")
           (println "-take-specific: take a specific item (example: to take :raw-egg, just type in raw-egg)")
           (println "-drop: drop an item in inventory (example: to drop :raw-egg, just type in raw-egg)")
           (println "-cookfish: cook the fish")
           (println "-cookegg: cook the egg")
           (println "-eat: eat something")
           (println "-unlock: unlock the chest to see what's inside")
           (println "-escape: escape from the grave and win the game (you will need a rope and be at the grave robber's tunnel)")
           (println "-quit: quit the game")
           (println " ")
         (let [choice (read-line)]
            (cond (= choice "move")
                  (do (println " ")
                      (println "Enter a canonical direction (example: n, N, north, go north)")
                      (println " ")
                      (let [dirchoice (read-line)]
                          (cond (or (or (or (= dirchoice "N") (= dirchoice "n")) (= dirchoice "north")) (= dirchoice "go north"))
                            (recur (status (go state :north)))
                                (or (or (or (= dirchoice "S") (= dirchoice "s")) (= dirchoice "south")) (= dirchoice "go south"))
                            (recur (status (go state :south)))
                                (or (or (or (= dirchoice "W") (= dirchoice "w")) (= dirchoice "west")) (= dirchoice "go west"))
                            (recur (status (go state :west)))
                                (or (or (or (= dirchoice "E") (= dirchoice "e")) (= dirchoice "east")) (= dirchoice "go east"))
                            (recur (status (go state :east)))
                                :else
                            (do (println " ")
                                (println "Invalid input, try again!")
                                (println " ")
                                (recur state)))))
                   (= choice "look")
                   (do (println " ")
                       (println (get-in rooms [location :desc]))
                       (println " ")
                       (recur state))
                   (= choice "check")
                   (do (println " ")
                       (println "-----------")
                       (run! println (get-in rooms [location :contents]))
                       (println "-----------")
                       (println " ")
                       (recur state))
                   (= choice "lifestat")
                   (do (println " ")
                       (println "number of lives:")
                       (println (get-in state [:adventurer :lives]))
                       (println " ")
                       (println "hp:")
                       (println (get-in state [:adventurer :hp]))
                       (println " ")
                       (recur state))
                   (= choice "inventory")
                   (do (println " ")
                       (println "-----------")
                       (run! println (get-in state [:adventurer :inventory]))
                       (println "-----------")
                       (println " ")
                       (recur state))
                   (= choice "take")
                   (do (println " ")
                       (println "You've taken up all the available items around")
                       (println " ")
                       (recur (take-items state)))
                   (= choice "drop")
                   (do (println " ")
                       (println "Which item you want to drop")
                       (println " ")
                       (let [dropkey (keyword (read-line))]
                            (recur (drop-items state dropkey))))
                   (= choice "cookfish")
                       (recur (cook-fish state))
                   (= choice "cookegg")
                       (recur (cook-egg state))
                   (= choice "eat")
                       (do (println " ")
                           (println "What do you want to eat?")
                           (println " ")
                           (let [eatkey (keyword (read-line))
                                 eatresult (eat state eatkey)]
                                 (if (= eatresult 1)
                                     (do (println " ")
                                         (println "You Died!")
                                         (println " "))
                                     (recur eatresult))))
                   (= choice "unlock")
                       (recur (unlock-chest state))
                   (= choice "escape")
                       (if (check-escape state)
                          (println "You successfully escape!, thanks for playing the game!")
                          (do (println " ")
                              (println "You do not meet the condition, keep working!")
                              (println " ")
                              (recur state)))
                   (= choice "examine")
                   (do (println " ")
                       (println "Which item you want to look into?")
                       (println " ")
                       (let [exkey (keyword (read-line))]
                            (if (contains? (get-in state [:adventurer :inventory]) exkey)
                              (do (println " ")
                                  (println (get-in state [:items exkey :desc]))
                                  (println " ")
                                  (recur state))
                              (do (println " ")
                                  (println "You do not have such item!")
                                  (println " ")
                                  (recur state)))))
                   (= choice "quit")
                       (do (println " ")
                           (println "Thanks for playing!")
                           (println " "))
                   (= choice "take-specific")
                   (do (println " ")
                       (println "Which item you want to take up")
                       (println " ")
                       (let [takekey (keyword (read-line))]
                            (recur (take-single-item state takekey))))
                   :else
                       (do (println " ")
                           (println "Invalid input, try again!")
                           (println " ")
                           (recur state))))))))
