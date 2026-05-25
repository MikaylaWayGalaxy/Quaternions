package Quaternion;

//***************************************************************************************************//
// Quaternions are composed of a real component, and 3 imaginary components.                         //
// They are in the form a + bi + cj + dk where a,b,c,d are real numbers and i,j,k are basis vectors  //
// for R3. Quaternions can also be represented as a + u where a is a real number and u is a vector   //
// in R3. They are useful for rotating objects in 3D.                                                //
//                                                                                                   //
// The basis vectors multiply as such:                                                               //
// i^2 = j^2 = k^2 = ijk = -1                                                                        //
// ij = k   ji = -k                                                                                  //
// jk = i   kj = -i                                                                                  //
// ki = j   ik = -j                                                                                  //
//                                                                                                   //
// Additional resources:                                                                             //
// https://lisyarus.github.io/blog/posts/introduction-to-quaternions.html                            //
// https://www.youtube.com/watch?v=d4EgbgTm0Bg                                                       //
// https://www.youtube.com/watch?v=htYh-Tq7ZBI                                                       //
//***************************************************************************************************//
public class Quaternion
{
    //***********************************************************************************************//
    //                                      private member variables                                 //
    //***********************************************************************************************//
    private double Real;        //Real component
    private double I;           //Component attached to i
    private double J;           //Component attached to j
    private double K;           //Component attached to j



    //***********************************************************************************************//
    //                                             constructors                                      //
    //***********************************************************************************************//
    //no-arg, initialize to all zeros
    public Quaternion()
    {
        Real = I = J = K = 0.0;
    }

    //takes in components and initializes
    public Quaternion(double Real, double I, double J, double K)
    {
        Quaternion.this.Real = Real;
        Quaternion.this.I = I;
        Quaternion.this.J = J;
        Quaternion.this.K = K;
    }

    //takes in a real number and a 3D vector and initializes
    public Quaternion(double Real, VectorR3 V)
    {
        Quaternion.this.Real = Real;
        I = V.GetX();
        J = V.GetY();
        K = V.GetZ();
    }

    //copy constructor
    public Quaternion(Quaternion Original)
    {
        Real = Original.GetReal();
        I = Original.GetI();
        J = Original.GetJ();
        K = Original.GetK();
    }
    //todo: check this



    //***********************************************************************************************//
    //                                      accessors and mutators                                   //
    //***********************************************************************************************//
    public double GetReal() { return Real; }
    public double GetI() { return I; }
    public double GetJ() { return J; }
    public double GetK() { return K; }
    public void SetReal(double Real) { Quaternion.this.Real = Real; }
    public void SetI(double I) { this.I = I; }
    public void SetJ(double J) { this.J = J; }
    public void SetK(double K) { this.K = K; }
    public VectorR3 GetVector() { return new VectorR3(I, J, K); }
    public void SetVector(VectorR3 Vector)
    {
        I = Vector.GetX();
        J = Vector.GetY();
        K = Vector.GetZ();
    }
    public void SetAll(Quaternion Q)
    {
        Real = Q.GetReal();
        I = Q.GetI();
        J = Q.GetJ();
        K = Q.GetK();
    }
    public void Copy(Quaternion Q) { SetAll(Q); }



    //***********************************************************************************************//
    //                                      public member functions                                  //
    //***********************************************************************************************//
    //checks equality
    //returns true if quaternions are equal, otherwise returns false
    public boolean Equals(Quaternion Q)
    {
        if (Real != Q.GetReal() || I != Q.GetI() || J != Q.GetJ() || K != Q.GetK())
            return false;
        return true;
    }

    //componentwise addition
    //adds a quaternion to this quaternion
    //note: function modifies this quaternion
    public Quaternion Add(Quaternion Q)
    {
        Real += Q.GetReal();
        I += Q.GetI();
        J += Q.GetJ();
        K += Q.GetK();
        return this;
    }

    //componentwise subtraction
    //adds a quaternion (scaled by -1) to this quaternion
    //note: function modifies this quaternion
    public Quaternion Subtract(Quaternion Q)
    {
        Real -= Q.GetReal();
        I -= Q.GetI();
        J -= Q.GetJ();
        K -= Q.GetK();
        return this;
    }

    //scalar multiplication
    //multiplies all components by a scalar
    //note: function modifies this quaternion
    public Quaternion Scale(double Scalar)
    {
        Real *= Scalar;
        I *= Scalar;
        J *= Scalar;
        K *= Scalar;
        return this;
    }

    //multiply this quaternion on the left by input Q
    //if Q is input and A is this, result is QA
    //note: function modifies this quaternion
    public void MultiplyLeft(Quaternion Q)
    {
        SetAll(Multiply(Q, this));
    }

    //multiply this quaternion on the right by Q
    //if Q is input and A is this, result is AQ
    //note: function modifies this quaternion
    public void MultiplyRight(Quaternion Q)
    {
        SetAll(Multiply(this, Q));
    }

    //returns magnitude aka length
    public double Magnitude()
    {
        return Math.sqrt( Math.pow(Real, 2.0) + Math.pow(I, 2.0) + Math.pow(J, 2.0) + Math.pow(K, 2.0) );
    }

    //make unit length, divide each component by the quaternion's magnitude
    //note: function modifies this quaternion
    public Quaternion Normalize()
    {
        if (Real == 0.0 && I == 0.0 && J == 0.0 && K == 0.0)            //todo: throw exception
            return this;
        return Scale(1.0 / Magnitude());
    }

    //returns the conjugate of this quaternion
    //note: does NOT modify this quaternion
    public Quaternion Conjugate()
    {
        return new Quaternion (Real, -I, -J, -K);
    }

    //returns the inverse of this quaternion
    //if unit quaternion, then inverse == conjugate
    //note: does NOT modify this quaternion
    public Quaternion Inverse()
    {
        if (Real == 0.0 && I == 0.0 && J == 0.0 && K == 0.0)         //if Magnitude == 0, then quaternion can't be inverted, causes divide by zero error if computed
            return this;
        //todo: throw divide by zero error or math error
        return new Quaternion(this.Conjugate().Scale(1.0 / (Math.pow(Real, 2.0) + Math.pow(I, 2.0) + Math.pow(J, 2.0) + Math.pow(K, 2.0))));
    }



    //***********************************************************************************************//
    //                                       static functions                                        //
    //***********************************************************************************************//
    //quaternion multiplication
    //noncommutative, order does matter
    //16 real number multiplications and 12 real number additions/subtractions
    public static Quaternion Multiply(Quaternion Left, Quaternion Right)
    {
        double Real = Left.GetReal()*Right.GetReal() - Left.GetI()*Right.GetI() - Left.GetJ()*Right.GetJ() - Left.GetK()*Right.GetK();
        double I =    Left.GetReal()*Right.GetI() + Left.GetI()*Right.GetReal() + Left.GetJ()*Right.GetK() - Left.GetK()*Right.GetJ();
        double J =    Left.GetReal()*Right.GetJ() + Left.GetJ()*Right.GetReal() - Left.GetI()*Right.GetK() + Left.GetK()*Right.GetI();
        double K =    Left.GetReal()*Right.GetK() + Left.GetK()*Right.GetReal() + Left.GetI()*Right.GetJ() - Left.GetJ()*Right.GetI();
        return new Quaternion(Real, I, J, K);
    }

    //multiply 3 quaternions
    //noncommutative, order does matter
    public static Quaternion Multiply3(Quaternion Left, Quaternion Middle, Quaternion Right)
    {
        return Multiply(Multiply(Left, Middle), Right);
    }

}
