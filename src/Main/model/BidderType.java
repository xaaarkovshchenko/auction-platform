package model;

/**
 * Enum zur Definition der verschiedenen Bieter-Strategien.
 *
 * Jeder Wert repräsentiert ein unterschiedliches Verhalten
 * bei der Entscheidung, ob ein Gebot abgegeben wird.
 */
public enum BidderType {

    /**
     * Aggressiver Bieter:
     * Bietet früh und häufig, auch bei höheren Preisen.
     */
    AGGRESSIVE,

    /**
     * Konservativer Bieter:
     * Wartet auf niedrige Preise und bietet vorsichtig.
     */
    CONSERVATIVE,

    /**
     * Sniper-Bieter:
     * Wartet bis zum Ende der Auktion und bietet dann schnell.
     */
    SNIPER
}