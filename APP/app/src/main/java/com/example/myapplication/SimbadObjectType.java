package com.example.myapplication;

import java.util.HashMap;
import java.util.Map;

public class SimbadObjectType {

    private static final Map<String, String> objectTypeDescriptions;

    static {
        objectTypeDescriptions = new HashMap<>();

        // Populate the map with object type descriptions
        objectTypeDescriptions.put("*", "Star: Represents individual stars.");
        objectTypeDescriptions.put("Ma*", "Massive Star: Initial mass > 8-10 Mo.");
        objectTypeDescriptions.put("bC*", "Beta Cep Variable: Massive star with pulsations.");
        objectTypeDescriptions.put("sg*", "Supergiant: Evolved massive star.");
        objectTypeDescriptions.put("s*r", "Red Supergiant: SpT like K/M 0,Ia,Iab,(I).");
        // ... Add descriptions for other object types

        // 1.1. Massive Stars and their Remnants
        objectTypeDescriptions.put("WR*", "Wolf-Rayet Star: SpT like W.");
        objectTypeDescriptions.put("N*", "Neutron Star: Remnants of massive stars after supernova explosions.");
        objectTypeDescriptions.put("Psr", "Pulsar: Rotating neutron star emitting beams of electromagnetic radiation.");
        // ... Add descriptions for other types in this category

        // 1.2. Young Stellar Objects (Pre-Main Sequence Stars)
        objectTypeDescriptions.put("Y*O", "Young Stellar Object: Pre-Main Sequence star.");
        objectTypeDescriptions.put("Or*", "Orion Variable: Variable star in the Orion constellation.");
        // ... Add descriptions for other types in this category

        // 1.3. Main Sequence Stars
        objectTypeDescriptions.put("MS*", "Main Sequence Star: Star in the main sequence phase of its evolution.");
        objectTypeDescriptions.put("Be*", "Be Star: B-type star showing emission lines in its spectrum.");
        // ... Add descriptions for other types in this category

        // 1.4. Evolved Stars
        objectTypeDescriptions.put("RGB*", "Red Giant Branch Star: Evolved star in the Red Giant Branch phase.");
        objectTypeDescriptions.put("HS*", "Hot Subdwarf: Hot, evolved star.");
        // ... Add descriptions for other types in this category

        // 1.5. Chemically Peculiar Stars
        objectTypeDescriptions.put("Pe*", "Chemically Peculiar Star: Star with unusual chemical abundances.");
        objectTypeDescriptions.put("a2*", "alpha2 CVn Variable: Ap star with magnetic field variations.");
        // ... Add descriptions for other types in this category

        // 1.6. Interacting Binaries and close Common Proper Motion Systems
        objectTypeDescriptions.put("**", "Double or Multiple Star: Interacting binaries and close CPM systems.");
        objectTypeDescriptions.put("SB*", "Spectroscopic Binary: Binary system with observable spectral lines.");
        // ... Add descriptions for other types in this category

        // 1.7. SuperNovae
        objectTypeDescriptions.put("SN*", "Supernova: Stellar explosion resulting in a bright, transient astronomical event.");
        // ... Add descriptions for other types in this category

        // 1.8. Low mass Stars and substellar Objects
        objectTypeDescriptions.put("LM*", "Low-mass Star: Star with mass less than 1 solar mass.");
        objectTypeDescriptions.put("BD*", "Brown Dwarf: Substellar object not massive enough to sustain hydrogen fusion.");
        // ... Add descriptions for other types in this category

        // 1.9. Properties, variability, spectral, kinematic or environment
        objectTypeDescriptions.put("V*", "Variable Star: Star exhibiting variability in its brightness.");
        objectTypeDescriptions.put("IR*", "Infra-Red Source: Source emitting infrared radiation.");
        objectTypeDescriptions.put("AVS", "Active Variable Star: Star with variable activity.");
        objectTypeDescriptions.put("Ir*", "Irregular Variable: Star showing irregular variations in brightness.");
        objectTypeDescriptions.put("Er*", "Eruptive Variable: Star with sudden outbursts or eruptions.");
        objectTypeDescriptions.put("RC*", "R CrB Variable: Variable star showing declines in brightness similar to R Coronae Borealis.");
        // ... Add descriptions for other types in this category

        objectTypeDescriptions.put("Pu*", "Pulsating Variable: Star exhibiting pulsations.");
        objectTypeDescriptions.put("SX*", "SX Phe Variable: Variable star showing SX Phe-type variations.");
        objectTypeDescriptions.put("gD*", "gamma Dor Variable: Variable star showing gamma Doradus-type variations.");
        objectTypeDescriptions.put("dS*", "delta Sct Variable: Variable star showing delta Scuti-type variations.");
        // ... Add descriptions for other types in this category

        // 1.10. Spectral properties
        objectTypeDescriptions.put("Em*", "Emission-line Star: Star with emission lines in its spectrum.");
        objectTypeDescriptions.put("AVS", "Active Variable Star: Star with variable activity.");
        // ... Add descriptions for other types in this category

        // 1.11. Kinematic and Environment Properties
        objectTypeDescriptions.put("PM*", "High Proper Motion Star: Star with high proper motion.");
        objectTypeDescriptions.put("HV*", "High Velocity Star: Star with high velocity.");
        // ... Add descriptions for other types in this category

        // 2. SETS OF STARS
        objectTypeDescriptions.put("Cl*", "Cluster of Stars: Collection of stars gravitationally bound together.");
        objectTypeDescriptions.put("GlC", "Globular Cluster: Spherical collection of stars around a galactic core.");
        // ... Add descriptions for other types in this category

        objectTypeDescriptions.put("OpC", "Open Cluster: Loose, irregularly shaped collection of stars.");
        objectTypeDescriptions.put("As*", "Association of Stars: Loose stellar association.");
        objectTypeDescriptions.put("St*", "Stellar Stream: Linear association of stars.");
        objectTypeDescriptions.put("MGr", "Moving Group: Coherent group of stars with common motion through space.");

        // 3. INTERSTELLAR MEDIUM
        objectTypeDescriptions.put("ISM", "Interstellar Medium Object: Object associated with the interstellar medium.");
        objectTypeDescriptions.put("AIM", "Astronomical Image: Astronomical image.");
        objectTypeDescriptions.put("SFR", "Star Forming Region: Region where new stars are being formed.");
        objectTypeDescriptions.put("HII", "HII Region: Region of ionized hydrogen gas.");
        objectTypeDescriptions.put("Cld", "Cloud: Interstellar cloud.");


        objectTypeDescriptions.put("GNe", "Galactic Nebula: Galactic nebula.");
        objectTypeDescriptions.put("RNe", "Reflection Nebula: Nebula that reflects the light of nearby stars.");
        objectTypeDescriptions.put("MoC", "Molecular Cloud: Dense region of interstellar gas and dust where stars form.");
        objectTypeDescriptions.put("DNe", "Dark Cloud: Cloud of gas and dust that is opaque, blocking background light.");
        objectTypeDescriptions.put("glb", "Globule: Small and dense region within a larger molecular cloud.");


        objectTypeDescriptions.put("CGb", "Cometary Globule / Pillar: Pillar-like structure within a molecular cloud.");
        objectTypeDescriptions.put("HVC", "High-velocity Cloud: Cloud of gas moving at a high velocity.");
        objectTypeDescriptions.put("cor", "Dense Core: Dense core within a molecular cloud.");
        objectTypeDescriptions.put("bub", "Bubble: Shell of ionized gas blown by stellar winds or supernovae.");
        objectTypeDescriptions.put("SNR", "SuperNova Remnant: Expanding shell of gas from a supernova explosion.");


        objectTypeDescriptions.put("sh", "Interstellar Shell: Shell-like structure in the interstellar medium.");
        objectTypeDescriptions.put("flt", "Interstellar Filament: Filamentary structure in the interstellar medium.");


        // 4. TAXONOMY OF GALAXIES
        objectTypeDescriptions.put("G", "Galaxy: Massive, gravitationally bound system of stars, stellar remnants, and dark matter.");
        objectTypeDescriptions.put("GGG", "Group of Galaxies: Small collection of galaxies.");
        objectTypeDescriptions.put("GiP", "Galaxy in Pair of Galaxies: Galaxy located in a pair of galaxies.");
        objectTypeDescriptions.put("GiG", "Galaxy towards a Group of Galaxies: Galaxy located towards a group of galaxies.");


        objectTypeDescriptions.put("GiC", "Galaxy towards a Cluster of Galaxies: Galaxy located towards a cluster of galaxies.");
        objectTypeDescriptions.put("GiG", "Galaxy towards a Nebula: Galaxy located towards a nebula.");
        objectTypeDescriptions.put("IG", "Interacting Galaxies: Galaxies that are interacting or merging.");
        objectTypeDescriptions.put("PaG", "Pair of Galaxies: Pair of galaxies.");


        objectTypeDescriptions.put("GrG", "Group of Galaxies: Collection of galaxies.");
        objectTypeDescriptions.put("CGG", "Compact Group of Galaxies: Small, dense group of galaxies.");
        objectTypeDescriptions.put("ClG", "Cluster of Galaxies: Large collection of galaxies.");
        objectTypeDescriptions.put("PCG", "Proto Cluster of Galaxies: Early stage of a cluster of galaxies.");


        objectTypeDescriptions.put("SCG", "Supercluster of Galaxies: Extremely large collection of galaxy clusters.");
        objectTypeDescriptions.put("vid", "Void: Underdense region in the large-scale structure of the universe.");

        // 5. SETS OF GALAXIES
        objectTypeDescriptions.put("OpC", "Open Cluster: Loose, irregularly shaped collection of stars.");
        objectTypeDescriptions.put("As*", "Association of Stars: Loose stellar association.");
        objectTypeDescriptions.put("St*", "Stellar Stream: Linear association of stars.");
        objectTypeDescriptions.put("MGr", "Moving Group: Coherent group of stars with common motion through space.");
        // ... Add descriptions for other types in this category

        // 6. GRAVITATION
        objectTypeDescriptions.put("grv", "Gravitational Source: Source associated with gravitational effects.");
        objectTypeDescriptions.put("Lev", "(Micro)Lensing Event: Gravitational microlensing event.");
        objectTypeDescriptions.put("gLS", "Gravitational Lens System: Gravitational lens system with lens and images.");
        objectTypeDescriptions.put("gLe", "Gravitational Lens: Gravitational lens bending light from a background source.");
        // ... Add descriptions for other types in this category

        objectTypeDescriptions.put("LeI", "Gravitationally Lensed Image: Gravitationally lensed image of an object.");
        objectTypeDescriptions.put("LeG", "Gravitationally Lensed Image of a Galaxy: Lensed image of a galaxy.");
        objectTypeDescriptions.put("LeQ", "Gravitationally Lensed Image of a Quasar: Lensed image of a quasar.");
        objectTypeDescriptions.put("BH", "Black Hole: Extremely dense region in space with strong gravitational effects.");
        // ... Add descriptions for other types in this category

        objectTypeDescriptions.put("GWE", "Gravitational Wave Event: Detection of gravitational waves from a significant event.");
        // ... Add descriptions for other types in this category

        // 7. GENERAL SPECTRAL PROPERTIES
        objectTypeDescriptions.put("ev", "Transient Event: Transient astronomical event.");
        objectTypeDescriptions.put("var", "Variable: Variable source.");
        objectTypeDescriptions.put("Rad", "Radio Source: Source emitting radio waves.");
        objectTypeDescriptions.put("mR", "Metric Radio Source: Radio source with emissions in the metric radio range.");
        // ... Add descriptions for other types in this category

        objectTypeDescriptions.put("cm", "Centimetric Radio Source: Radio source with emissions in the centimetric radio range.");
        objectTypeDescriptions.put("mm", "Millimetric Radio Source: Radio source with emissions in the millimetric radio range.");
        objectTypeDescriptions.put("smm", "Sub-Millimetric Source: Source emitting sub-millimetric waves.");
        objectTypeDescriptions.put("HI", "HI (21cm) Source: Source emitting hydrogen line radiation at 21cm wavelength.");
        // ... Add descriptions for other types in this category

        objectTypeDescriptions.put("rB", "Radio Burst: Burst of radio waves from a celestial source.");
        objectTypeDescriptions.put("Mas", "Maser: Celestial source emitting microwave amplification by stimulated emission of radiation.");
        objectTypeDescriptions.put("IR", "Infrared Source: Source emitting infrared radiation.");
        objectTypeDescriptions.put("FIR", "Far-IR Source: Source emitting far-infrared radiation (λ >= 30 µm).");
        // ... Add descriptions for other types in this category

        objectTypeDescriptions.put("MIR", "Mid-IR Source: Source emitting mid-infrared radiation (3 to 30 µm).");
        objectTypeDescriptions.put("NIR", "Near-IR Source: Source emitting near-infrared radiation (λ < 3 µm).");
        objectTypeDescriptions.put("Opt", "Optical Source: Source observable in the optical range (BVRI).");
        objectTypeDescriptions.put("EmO", "Emission Object: Object emitting electromagnetic radiation.");
        // ... Add descriptions for other types in this category

        objectTypeDescriptions.put("blu", "Blue Object: Object with a blue color, typically white dwarfs, hot subdwarfs, or blue horizontal branch stars.");
        objectTypeDescriptions.put("UV", "UV Source: Source emitting ultraviolet radiation.");
        objectTypeDescriptions.put("X", "X-ray Source: Source emitting X-rays.");
        objectTypeDescriptions.put("ULX", "Ultra-luminous X-ray Source: X-ray source with exceptionally high luminosity.");
        // ... Add descriptions for other types in this category

        objectTypeDescriptions.put("gam", "Gamma-ray Source: Source emitting gamma rays.");
        objectTypeDescriptions.put("gB", "Gamma-ray Burst: Burst of gamma rays.");
        // ... Add descriptions for other types in this category

        // 8. BLENDS, ERRORS, NOT WELL DEFINED OBJECTS
        objectTypeDescriptions.put("mul", "Blend: Composite object or blend of multiple sources.");
        objectTypeDescriptions.put("err", "Inexistent: Not an object (Error, Artefact, ...).");
        objectTypeDescriptions.put("PoC", "Part of Cloud: Celestial object part of an interstellar cloud.");
        objectTypeDescriptions.put("PoG", "Part of a Galaxy: Celestial object part of a galaxy.");
        // ... Add descriptions for other types in this category

        objectTypeDescriptions.put("?", "Unknown: Object of unknown nature.");
        objectTypeDescriptions.put("reg", "Region: Defined region in the sky.");

    }

    /**
     * Get the description for a given object type.
     *
     * @param objectType The type of the celestial object.
     * @return The description of the object type, or null if not found.
     */
    public static String getDescription(String objectType) {
        return objectTypeDescriptions.get(objectType);
    }
}
