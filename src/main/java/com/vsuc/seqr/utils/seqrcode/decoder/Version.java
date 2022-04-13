package com.vsuc.seqr.utils.seqrcode.decoder;

import com.vsuc.seqr.utils.classes.BitMatrix;
import com.vsuc.seqr.utils.exceptions.FormatException;

public final class Version {


    private static final int[] VERSION_DECODE_INFO = {
            0x07C94, 0x085BC, 0x09A99, 0x0A4D3, 0x0BBF6,
            0x0C762, 0x0D847, 0x0E60D, 0x0F928, 0x10B78,
            0x1145D, 0x12A17, 0x13532, 0x149A6, 0x15683,
            0x168C9, 0x177EC, 0x18EC4, 0x191E1, 0x1AFAB,
            0x1B08E, 0x1CC1A, 0x1D33F, 0x1ED75, 0x1F250,
            0x209D5, 0x216F0, 0x228BA, 0x2379F, 0x24B0B,
            0x2542E, 0x26A64, 0x27541, 0x28C69
    };


    private final int versionNumber;
    private final int[] alignmentPatternCenters;
    private final ECBlocks[] ecBlocks;
    private final int totalCodewords;

    private Version(int versionNumber,
                    int[] alignmentPatternCenters,
                    ECBlocks... ecBlocks) {
        this.versionNumber = versionNumber;
        this.alignmentPatternCenters = alignmentPatternCenters;
        this.ecBlocks = ecBlocks;
        int total = 0;
        int ecCodewords = ecBlocks[0].getECCodewordsPerBlock();
        ECB[] ecbArray = ecBlocks[0].getECBlocks();
        for (ECB ecBlock : ecbArray) {
            total += ecBlock.getCount() * (ecBlock.getDataCodewords() + ecCodewords);
        }
        this.totalCodewords = total;
    }

    public ECBlocks getECBlocksForLevel(ErrorCorrectionLevel ecLevel) {
        return ecBlocks[ecLevel.ordinal()];
    }

    public int getTotalCodewords() {
        return totalCodewords;
    }

    public static Version recommendVersion() {
        return new Version(1, new int[]{},
                new ECBlocks(7, new ECB(1, 19)),
                new ECBlocks(10, new ECB(1, 16)),
                new ECBlocks(13, new ECB(1, 13)),
                new ECBlocks(17, new ECB(1, 9)));
    }


    /**
     * <p>Encapsulates a set of error-correction blocks in one symbol version. Most versions will
     * use blocks of differing sizes within one version, so, this encapsulates the parameters for
     * each set of blocks. It also holds the number of error-correction codewords per block since it
     * will be the same across all blocks within one version.</p>
     */
    public static final class ECBlocks {
        private final int ecCodewordsPerBlock;
        private final ECB[] ecBlocks;

        ECBlocks(int ecCodewordsPerBlock, ECB... ecBlocks) {
            this.ecCodewordsPerBlock = ecCodewordsPerBlock;
            this.ecBlocks = ecBlocks;
        }

        public int getECCodewordsPerBlock() {
            return ecCodewordsPerBlock;
        }

        public int getNumBlocks() {
            int total = 0;
            for (ECB ecBlock : ecBlocks) {
                total += ecBlock.getCount();
            }
            return total;
        }

        public int getTotalECCodewords() {
            return ecCodewordsPerBlock * getNumBlocks();
        }

        public ECB[] getECBlocks() {
            return ecBlocks;
        }
    }

    /**
     * <p>Encapsulates the parameters for one error-correction block in one symbol version.
     * This includes the number of data codewords, and the number of times a block with these
     * parameters is used consecutively in the QR code version's format.</p>
     */
    public static final class ECB {
        private final int count;
        private final int dataCodewords;

        ECB(int count, int dataCodewords) {
            this.count = count;
            this.dataCodewords = dataCodewords;
        }

        public int getCount() {
            return count;
        }

        public int getDataCodewords() {
            return dataCodewords;
        }
    }

    public int getDimensionForVersion() {
        return 17 + 4 * versionNumber;
    }

    public static Version getVersionForNumber(int versionNumber) {
        if (versionNumber < 1 || versionNumber > 40) {
            throw new IllegalArgumentException();
        }
        return new Version(1, new int[]{},
                new ECBlocks(7, new ECB(1, 19)),
                new ECBlocks(10, new ECB(1, 16)),
                new ECBlocks(13, new ECB(1, 13)),
                new ECBlocks(17, new ECB(1, 9)));
    }

    static Version decodeVersionInformation(int versionBits) {
        int bestDifference = Integer.MAX_VALUE;
        int bestVersion = 0;
        for (int i = 0; i < VERSION_DECODE_INFO.length; i++) {
            int targetVersion = VERSION_DECODE_INFO[i];
            // Do the version info bits match exactly? done.
            if (targetVersion == versionBits) {
                return getVersionForNumber(i + 7);
            }
            // Otherwise see if this is the closest to a real version info bit string
            // we have seen so far
            int bitsDifference = FormatInformation.numBitsDiffering(versionBits, targetVersion);
            if (bitsDifference < bestDifference) {
                bestVersion = i + 7;
                bestDifference = bitsDifference;
            }
        }
        // We can tolerate up to 3 bits of error since no two version info codewords will
        // differ in less than 8 bits.
        if (bestDifference <= 3) {
            return getVersionForNumber(bestVersion);
        }
        // If we didn't find a close enough match, fail
        return null;
    }


    BitMatrix buildFunctionPattern() {
        int dimension = getDimensionForVersion();
        BitMatrix bitMatrix = new BitMatrix(dimension);

        // Top left finder pattern + separator + format
        bitMatrix.setRegion(0, 0, 9, 9);
        // Top right finder pattern + separator + format
        bitMatrix.setRegion(dimension - 8, 0, 8, 9);
        // Bottom left finder pattern + separator + format
        bitMatrix.setRegion(0, dimension - 8, 9, 8);

        // Alignment patterns
        int max = alignmentPatternCenters.length;
        for (int x = 0; x < max; x++) {
            int i = alignmentPatternCenters[x] - 2;
            for (int y = 0; y < max; y++) {
                if ((x != 0 || (y != 0 && y != max - 1)) && (x != max - 1 || y != 0)) {
                    bitMatrix.setRegion(alignmentPatternCenters[y] - 2, i, 5, 5);
                }
                // else no o alignment patterns near the three finder patterns
            }
        }

        // Vertical timing pattern
        bitMatrix.setRegion(6, 9, 1, dimension - 17);
        // Horizontal timing pattern
        bitMatrix.setRegion(9, 6, dimension - 17, 1);

        if (versionNumber > 6) {
            // Version info, top right
            bitMatrix.setRegion(dimension - 11, 0, 3, 6);
            // Version info, bottom left
            bitMatrix.setRegion(0, dimension - 11, 6, 3);
        }

        return bitMatrix;
    }

    /**
     * <p>Deduces version information purely from QR Code dimensions.</p>
     *
     * @param dimension dimension in modules
     * @return Version for a QR Code of that dimension
     * @throws FormatException if dimension is not 1 mod 4
     */
    public static Version getProvisionalVersionForDimension(int dimension) throws FormatException {
        if (dimension % 4 != 1) {
            throw FormatException.getFormatInstance();
        }
        try {
            return getVersionForNumber((dimension - 17) / 4);
        } catch (IllegalArgumentException ignored) {
            throw FormatException.getFormatInstance();
        }
    }

}
