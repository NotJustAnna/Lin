# Lin

An attempt to create a parser and interpreter for a language which resembles a lot Kotlin but borrow elements from JavaScript... and Rust.

Lin will never fully parse Kotlin, but hit really closes to home.

## Implemented

- Booleans, Null
- Ints, Longs, Floats, Doubles, Chars
- Strings (with support for raw and template strings)
- Variables
    - Vals, Vars, Destructuring variables
- Access/assignments of variables
- Function invocation
- Objects
- Infix function invocation
- Property access/assignment
- While/Do-While loops
- For Loops
    - **CAVEATS**:
        - For loops doesn't support destructuring variables
- If/else (both as statement and expression)
- Return/Throw
- Null-safe operator (`?.`)
- Not-null assertion operator (`!!`)
- Unary operations: `!a`, `+a`, `-a`
- Binary operations:
    - `a+b`, `a-b`, `a*b`, `a/b`, `a%b`
    - `a==b`, `a!=b`, `a&&b`, `a||b`, `a<b`, `a<=b`, `a>b`, `a>=b`
    - `a ?: b`, `a in b`, `a !in b`, `a..b`
- Function declaration (named, anonymous)
- Subscript access/assignment (`a[...]`, `a[...] = b`)
- Increment (`++a`, `a++`) and Decrement (`--a`, `a--`) operations
- Assign operations: `a+=b`, `a-=b`, `a*=b`, `a/=b`, `a%=b`
- Objects
- Try, Catch and Throw
    - **CAVEAT**: There are no types. Anything can be thrown.

## Parsing

- Classes and Interfaces
    - Support for Companion Objects, Enum classes, Functional Interfaces (Kotlin 1.4+)
    - Support for Class Constructors and Interface delegation
    - **CAVEATS**:
        - Data, Sealed and Annotation classes are supported only as Modifiers
        - Enum class doesn't support anything fancy
- Modifiers 
    - Abstract, Sealed, Data, Annotation
    - Open, Final, Override, Const
    - Public, Internal, Protected, Private
    - Infix, Operator, Lateinit
- Property delegation

## Missing

- The type system, and by extent:
    - Inheritance in objects, classes and interfaces
    - `is`, `!is`, `as`, `as?`, `typeof` operators
    - Types on variables, parameters, return types
    - SAM constructors, if they get implemented at all
    - Generics, if they get implemented at all
    - Type aliases, if they get implemented at all
- Declaration of extension functions (Requires a minimal type system)
- Declaration of function-based properties
- Break/Continue on Loops
- Labels on Loops
- When
- Lambdas
- Modifiers don't get validated
- The interpreter

## Stuff that is different
- Lambdas
    - They can be declared pretty much anywhere, no need for a magic type.
    - Any parameters must be enclosed in pipes (`|`)
        - `a { doSomething() }` stays the same
        - `a { it.values }` stays the same
        - `a { b -> c(b) }` turns into `a { | b | -> c(b) }`
        - `a { b, c -> c(b) }` turns into `a { | b, c | -> c(b) }`
        - `a { (b, c) -> c(b) }` turns into `a { | (b, c) | -> c(b) }`
- Inheritance declarations
    - Inheritances must be enclosed in brackets.
        - `class A` stays the same
        - `class A(val a)` stays the same
        - `class A : Iterable` turns into `class A [Iterable]`
        - `class A(val a) : Iterable` turns into `class A(val a) [Iterable]`
- Function overloading won't ever be implemented.

## Stuff that will most likely be different

- Mostly everything regarding imports/packages will be different.
    - **Lin** will most likely follow Python/Javascript ideas in this scenario.
    - It is almost sure that `package x; import ...` won't happen.
- All **Lin** files will be script files.
    - This means that loading classes means loading and executing the script to get the classes.
- Inline and _tailrec_ functions will most likely never get implemented.
- Standard library and everything related (Coroutines, Reflections)
- Exceptions
- Annotations