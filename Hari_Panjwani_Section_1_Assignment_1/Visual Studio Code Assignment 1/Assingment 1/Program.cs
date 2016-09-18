using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Assingment_1
{
    class Program
    {
        static void Main(string[] args)
        {
            // function to build the prime numbers:
            Console.WriteLine("Hit RETURN to generate the prime numbers transofmration..");
            Console.ReadLine();

            // calling UnfoldPrime with seed as 2 and number is incremnet by 2, if number is anything
            // other than 2. Increment by 2 is done because prime number occur only at odd places
            var primeNumbers = UnfoldPrime(2, number => (number == 2) ? number + 1 : number + 2);
            
            // calling the defination or transformation of the prime numbers referred by compiler
            Console.WriteLine("This is how the compiler refers to the transformation:");
            Console.WriteLine(primeNumbers);
            Console.WriteLine("Now that we have a transformation that describes how prime numbers are built, let's generate a few..");
            Console.ReadLine();

            // print the first 10 prime numbers
            Console.WriteLine("Hit RETURN to generate the first 10 prime numbers..");
            Console.ReadLine();
            foreach (var x in primeNumbers.Take(10))
            {
                Console.WriteLine(x);
            }
            Console.ReadLine();

            // print the 10001st prime number
            Console.WriteLine("The 10001st Prime number (press return key to generate):");
            Console.ReadLine();

            // Applying functions on prime number transformation, using the skip function
            // to Skip 10000 numbers from the collection after that using Take to take the
            // first number after 10000 and print using First()
            // Result is 104743
            var result = primeNumbers.Skip(10000).Take(1).First();
            Console.WriteLine(result);
            Console.ReadLine();

            // function to build the fibonacci of prime numbers:
            Console.WriteLine("Hit RETURN to generate the primonacci numbers transofmration..");
            Console.ReadLine();

            // calling UnfoldPrimonacci the transformation of primonacci, it take 2 input and return
            // the sum of those two numbers which is next number to call the transformation
            var primonacci = UnfoldPrimonacci(1, (a,b) => (a+b));

            // calling the definition or transformation of the primonacci numbers referred by compiler 
            Console.WriteLine("This is how the compiler refers to the transformation:");
            Console.WriteLine(primonacci);
            Console.WriteLine("Now that we have a transformation that describes how fibonacci of prime numbers are built, let's generate a few..");
            Console.ReadLine();

            // print the first 20 numbers of primonacci series, as you must have observed the
            //  pattern start giving random numbers after 12th number. The numbers is that big 
            // that it is not confined to int32 or int64. therefore it started giving random number.
            Console.WriteLine("Hit RETURN to generate the first 20 primonacci numbers..");
            Console.ReadLine();
            foreach (var x in primonacci.Take(20))
            {
                Console.WriteLine(x);
            }
            Console.ReadLine();

            // print the 10001st fibonacci of prime number
            // Initially when I printed the 10001st number, it gave 7234079, it clearly seems the
            // wrong number as it is 7 digit number whereas the 10th number in the series itself is of 9 digit.
            // Then I went back to print the 9500th and 9800th number in the series then I realized the number 
            // generated is random and decide to print first 20 numbers and saw the pattern breaks after 12th number.       
            Console.WriteLine("The 10001st Fibonacci of Prime number (press return key to generate):");
            Console.ReadLine();
            var resultPrime = primonacci.Skip(10000).Take(1).First();
            Console.WriteLine(resultPrime);
            Console.ReadLine();
        }

        // [][] prime numbers transformation (coroutine)
        private static IEnumerable<T> UnfoldPrime<T>(T seed, Func<T, T> accumulator)
        {
            var number = seed;
            while (true)
            {
                // convert enumeration to int64 so that we can check
                // if the number is 2, we should that itself
                var a = Convert.ToInt64(number);

                if (a == 2)
                    yield return number; 
                
                // Enumeration is a for loop which is running from 2 to the square root of
                // the number and if for all the number in the loop remainder is not equal to
                // zero, it gives boolean value as true, otherwise as false                         
                if (Enumerable.Range(2, (int)Math.Sqrt(a))
                             .All(divisor => a % divisor != 0))
                    yield return number;
                
                number = accumulator(number);
            }
        }

        // [][] fibonacci of prime numbers transformation (coroutine)
        private static IEnumerable<T> UnfoldPrimonacci<T>(T seed, Func<T, T, T> accumulator)
        {
            var a = seed;
            var b = seed;
            T c;
            while (true)
            {
                // if the number is prime in the fibonacci series then return,
                // otherwise continue the loop, this well help to find the next prime
                // number in the fibonacci series
                if(isPrime(Convert.ToInt64(b)))
                    yield return b;
                
                c = b;
                b = accumulator(a, b);
                a = c;
            }
        }

        // function to find out whether the number is prime or not
        private static bool isPrime(Int64 num)
        {
            // if the number is less than 2, return false
            if (num < 2)
                return false;
            // if the number is 2 return true
            else if (num == 2)
                return true;
            // Enumeration is a for loop which is running from 2 to the square root of
            // the number and if for all the number in the loop remainder is not equal to
            // zero, it gives boolean value as true, otherwise as false 
            else if (Enumerable.Range(2, (int)Math.Ceiling(Math.Sqrt(num)))
                         .All(divisor => num % divisor != 0))
                return true;
            else
                return false;
        }
    }
}
