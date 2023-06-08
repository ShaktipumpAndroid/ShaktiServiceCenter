package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class DistanceResponse {

    @SerializedName("geocoded_waypoints")
    @Expose
    private List<GeocodedWaypoint> geocodedWaypoints;
    @SerializedName("routes")
    @Expose
    private List<Route> routes;
    @SerializedName("status")
    @Expose
    private String status;

    public List<GeocodedWaypoint> getGeocodedWaypoints() {
        return geocodedWaypoints;
    }

    public void setGeocodedWaypoints(List<GeocodedWaypoint> geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class GeocodedWaypoint {

        @SerializedName("geocoder_status")
        @Expose
        private String geocoderStatus;
        @SerializedName("place_id")
        @Expose
        private String placeId;
        @SerializedName("types")
        @Expose
        private List<String> types;

        public String getGeocoderStatus() {
            return geocoderStatus;
        }

        public void setGeocoderStatus(String geocoderStatus) {
            this.geocoderStatus = geocoderStatus;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

    }

    public static class Route {

        @SerializedName("bounds")
        @Expose
        private Bounds bounds;
        @SerializedName("copyrights")
        @Expose
        private String copyrights;
        @SerializedName("legs")
        @Expose
        private List<Leg> legs;
        @SerializedName("overview_polyline")
        @Expose
        private OverviewPolyline overviewPolyline;
        @SerializedName("summary")
        @Expose
        private String summary;
        @SerializedName("warnings")
        @Expose
        private List<Object> warnings;
        @SerializedName("waypoint_order")
        @Expose
        private List<Object> waypointOrder;

        public Bounds getBounds() {
            return bounds;
        }

        public void setBounds(Bounds bounds) {
            this.bounds = bounds;
        }

        public String getCopyrights() {
            return copyrights;
        }

        public void setCopyrights(String copyrights) {
            this.copyrights = copyrights;
        }

        public List<Leg> getLegs() {
            return legs;
        }

        public void setLegs(List<Leg> legs) {
            this.legs = legs;
        }

        public OverviewPolyline getOverviewPolyline() {
            return overviewPolyline;
        }

        public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
            this.overviewPolyline = overviewPolyline;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public List<Object> getWarnings() {
            return warnings;
        }

        public void setWarnings(List<Object> warnings) {
            this.warnings = warnings;
        }

        public List<Object> getWaypointOrder() {
            return waypointOrder;
        }

        public void setWaypointOrder(List<Object> waypointOrder) {
            this.waypointOrder = waypointOrder;
        }
        public static class Bounds {

            @SerializedName("northeast")
            @Expose
            private Northeast northeast;
            @SerializedName("southwest")
            @Expose
            private Southwest southwest;

            public Northeast getNortheast() {
                return northeast;
            }

            public void setNortheast(Northeast northeast) {
                this.northeast = northeast;
            }

            public Southwest getSouthwest() {
                return southwest;
            }

            public void setSouthwest(Southwest southwest) {
                this.southwest = southwest;
            }
            public static class Northeast {

                @SerializedName("lat")
                @Expose
                private Double lat;
                @SerializedName("lng")
                @Expose
                private Double lng;

                public Double getLat() {
                    return lat;
                }

                public void setLat(Double lat) {
                    this.lat = lat;
                }

                public Double getLng() {
                    return lng;
                }

                public void setLng(Double lng) {
                    this.lng = lng;
                }

            }


            public static class Southwest {

                @SerializedName("lat")
                @Expose
                private Double lat;
                @SerializedName("lng")
                @Expose
                private Double lng;

                public Double getLat() {
                    return lat;
                }

                public void setLat(Double lat) {
                    this.lat = lat;
                }

                public Double getLng() {
                    return lng;
                }

                public void setLng(Double lng) {
                    this.lng = lng;
                }

            }
        }
        public static class OverviewPolyline {

            @SerializedName("points")
            @Expose
            private String points;

            public String getPoints() {
                return points;
            }

            public void setPoints(String points) {
                this.points = points;
            }

        }



        public class Leg {

            @SerializedName("distance")
            @Expose
            private Distance distance;
            @SerializedName("duration")
            @Expose
            private Duration duration;
            @SerializedName("end_address")
            @Expose
            private String endAddress;
            @SerializedName("end_location")
            @Expose
            private EndLocation endLocation;
            @SerializedName("start_address")
            @Expose
            private String startAddress;
            @SerializedName("start_location")
            @Expose
            private StartLocation startLocation;
            @SerializedName("steps")
            @Expose
            private List<Step> steps;
            @SerializedName("traffic_speed_entry")
            @Expose
            private List<Object> trafficSpeedEntry;
            @SerializedName("via_waypoint")
            @Expose
            private List<Object> viaWaypoint;

            public Distance getDistance() {
                return distance;
            }

            public void setDistance(Distance distance) {
                this.distance = distance;
            }

            public Duration getDuration() {
                return duration;
            }

            public void setDuration(Duration duration) {
                this.duration = duration;
            }

            public String getEndAddress() {
                return endAddress;
            }

            public void setEndAddress(String endAddress) {
                this.endAddress = endAddress;
            }

            public EndLocation getEndLocation() {
                return endLocation;
            }

            public void setEndLocation(EndLocation endLocation) {
                this.endLocation = endLocation;
            }

            public String getStartAddress() {
                return startAddress;
            }

            public void setStartAddress(String startAddress) {
                this.startAddress = startAddress;
            }

            public StartLocation getStartLocation() {
                return startLocation;
            }

            public void setStartLocation(StartLocation startLocation) {
                this.startLocation = startLocation;
            }

            public List<Step> getSteps() {
                return steps;
            }

            public void setSteps(List<Step> steps) {
                this.steps = steps;
            }

            public List<Object> getTrafficSpeedEntry() {
                return trafficSpeedEntry;
            }

            public void setTrafficSpeedEntry(List<Object> trafficSpeedEntry) {
                this.trafficSpeedEntry = trafficSpeedEntry;
            }

            public List<Object> getViaWaypoint() {
                return viaWaypoint;
            }

            public void setViaWaypoint(List<Object> viaWaypoint) {
                this.viaWaypoint = viaWaypoint;
            }

            public  class Distance {

                @SerializedName("text")
                @Expose
                private String text;
                @SerializedName("value")
                @Expose
                private Integer value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public Integer getValue() {
                    return value;
                }

                public void setValue(Integer value) {
                    this.value = value;
                }

            }

            public  class Duration {

                @SerializedName("text")
                @Expose
                private String text;
                @SerializedName("value")
                @Expose
                private Integer value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public Integer getValue() {
                    return value;
                }

                public void setValue(Integer value) {
                    this.value = value;
                }

            }
            public  class StartLocation {

                @SerializedName("lat")
                @Expose
                private Double lat;
                @SerializedName("lng")
                @Expose
                private Double lng;

                public Double getLat() {
                    return lat;
                }

                public void setLat(Double lat) {
                    this.lat = lat;
                }

                public Double getLng() {
                    return lng;
                }

                public void setLng(Double lng) {
                    this.lng = lng;
                }

            }

            public  class EndLocation {

                @SerializedName("lat")
                @Expose
                private Double lat;
                @SerializedName("lng")
                @Expose
                private Double lng;

                public Double getLat() {
                    return lat;
                }

                public void setLat(Double lat) {
                    this.lat = lat;
                }

                public Double getLng() {
                    return lng;
                }

                public void setLng(Double lng) {
                    this.lng = lng;
                }

            }

            public  class Step {

                @SerializedName("distance")
                @Expose
                private Distance__1 distance;
                @SerializedName("duration")
                @Expose
                private Duration__1 duration;
                @SerializedName("end_location")
                @Expose
                private EndLocation__1 endLocation;
                @SerializedName("html_instructions")
                @Expose
                private String htmlInstructions;
                @SerializedName("polyline")
                @Expose
                private Polyline polyline;
                @SerializedName("start_location")
                @Expose
                private StartLocation__1 startLocation;
                @SerializedName("travel_mode")
                @Expose
                private String travelMode;
                @SerializedName("maneuver")
                @Expose
                private String maneuver;

                public Distance__1 getDistance() {
                    return distance;
                }

                public void setDistance(Distance__1 distance) {
                    this.distance = distance;
                }

                public Duration__1 getDuration() {
                    return duration;
                }

                public void setDuration(Duration__1 duration) {
                    this.duration = duration;
                }

                public EndLocation__1 getEndLocation() {
                    return endLocation;
                }

                public void setEndLocation(EndLocation__1 endLocation) {
                    this.endLocation = endLocation;
                }

                public String getHtmlInstructions() {
                    return htmlInstructions;
                }

                public void setHtmlInstructions(String htmlInstructions) {
                    this.htmlInstructions = htmlInstructions;
                }

                public Polyline getPolyline() {
                    return polyline;
                }

                public void setPolyline(Polyline polyline) {
                    this.polyline = polyline;
                }

                public StartLocation__1 getStartLocation() {
                    return startLocation;
                }

                public void setStartLocation(StartLocation__1 startLocation) {
                    this.startLocation = startLocation;
                }

                public String getTravelMode() {
                    return travelMode;
                }

                public void setTravelMode(String travelMode) {
                    this.travelMode = travelMode;
                }

                public String getManeuver() {
                    return maneuver;
                }

                public void setManeuver(String maneuver) {
                    this.maneuver = maneuver;
                }

                public  class Distance__1 {

                    @SerializedName("text")
                    @Expose
                    private String text;
                    @SerializedName("value")
                    @Expose
                    private Integer value;

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }

                    public Integer getValue() {
                        return value;
                    }

                    public void setValue(Integer value) {
                        this.value = value;
                    }

                }
                public  class Duration__1 {

                    @SerializedName("text")
                    @Expose
                    private String text;
                    @SerializedName("value")
                    @Expose
                    private Integer value;

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }

                    public Integer getValue() {
                        return value;
                    }

                    public void setValue(Integer value) {
                        this.value = value;
                    }

                }
                public  class StartLocation__1 {

                    @SerializedName("lat")
                    @Expose
                    private Double lat;
                    @SerializedName("lng")
                    @Expose
                    private Double lng;

                    public Double getLat() {
                        return lat;
                    }

                    public void setLat(Double lat) {
                        this.lat = lat;
                    }

                    public Double getLng() {
                        return lng;
                    }

                    public void setLng(Double lng) {
                        this.lng = lng;
                    }

                }

                public class EndLocation__1 {

                    @SerializedName("lat")
                    @Expose
                    private Double lat;
                    @SerializedName("lng")
                    @Expose
                    private Double lng;

                    public Double getLat() {
                        return lat;
                    }

                    public void setLat(Double lat) {
                        this.lat = lat;
                    }

                    public Double getLng() {
                        return lng;
                    }

                    public void setLng(Double lng) {
                        this.lng = lng;
                    }

                }

                public class Polyline {

                    @SerializedName("points")
                    @Expose
                    private String points;

                    public String getPoints() {
                        return points;
                    }

                    public void setPoints(String points) {
                        this.points = points;
                    }

                }

            }







        }
    }
}
