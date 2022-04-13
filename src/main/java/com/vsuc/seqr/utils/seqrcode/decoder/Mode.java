package com.vsuc.seqr.utils.seqrcode.decoder;

public enum Mode {

    TERMINATOR(new int[]{0, 0, 0}, 0x00), // Not really a mode...
    NUMERIC(new int[]{10, 12, 14}, 0x01),
    ALPHANUMERIC(new int[]{9, 11, 13}, 0x02),
    BYTE(new int[]{8, 16, 16}, 0x04),
    KANJI(new int[]{8, 10, 12}, 0x08);

    private final int[] characterCountBitsForVersions;
    private final int bits;

    Mode(int[] characterCountBitsForVersions, int bits) {
        this.characterCountBitsForVersions = characterCountBitsForVersions;
        this.bits = bits;
    }

    public static Mode forBits(int bits) {
        switch (bits) {
            case 0x0:
                return TERMINATOR;
            case 0x1:
                return NUMERIC;
            case 0x2:
                return ALPHANUMERIC;
            case 0x4:
                return BYTE;
            case 0x8:
                return KANJI;
            default:
                throw new IllegalArgumentException();
        }
    }

//    public int getCharacterCountBits(Version version) {
//        int number = version.getVersionNumber();
//        int offset;
//        if (number <= 9) {
//            offset = 0;
//        } else if (number <= 26) {
//            offset = 1;
//        } else {
//            offset = 2;
//        }
//        return characterCountBitsForVersions[offset];
//    }

    public int getBits() {
        return bits;
    }

    public int getCharacterCountBits(Version version) {
        int offset = 0;
        return characterCountBitsForVersions[offset];
    }

}
